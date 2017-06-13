/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.Serializable;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.framework.qual.AnnotatedFor;
/**
 * This class turns a Java FileFilter or FilenameFilter into an IO FileFilter.
 *
 * @since 1.0
 * @version $Id$
 *
 * @see FileFilterUtils#asFileFilter(FileFilter)
 * @see FileFilterUtils#asFileFilter(FilenameFilter)
 *
 * filenameFilter, fileFilter can have null values, which are assigned at runtime
 * based on use of constructor.
 */
@AnnotatedFor({"nullness"})
public class DelegateFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = -8723373124984771318L;
    /** The Filename filter */
    private final @Nullable FilenameFilter filenameFilter;
    /** The File filter */
    private final @Nullable FileFilter fileFilter;

    /**
     * Constructs a delegate file filter around an existing FilenameFilter.
     *
     * @param filter  the filter to decorate
     */
    public DelegateFileFilter(final FilenameFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The FilenameFilter must not be null");
        }
        this.filenameFilter = filter;
        this.fileFilter = null;
    }

    /**
     * Constructs a delegate file filter around an existing FileFilter.
     *
     * @param filter  the filter to decorate
     */
    public DelegateFileFilter(final FileFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The FileFilter must not be null");
        }
        this.fileFilter = filter;
        this.filenameFilter = null;
    }

    /**
     * Checks the filter.
     *
     * @param file  the file to check
     * @return true if the filter matches
     */
    @Override
    public boolean accept(final File file) {
        if (fileFilter != null) {
            return fileFilter.accept(file);
        } else {
            return super.accept(file);
        }
    }

    /**
     * Checks the filter.
     *
     * @param dir  the directory
     * @param name  the filename in the directory
     * @return true if the filter matches
     */
    @Override
    public boolean accept(final File dir, final String name) {
        if (filenameFilter != null) {
            return filenameFilter.accept(dir, name);
        } else {
            return super.accept(dir, name);
        }
    }

    /**
     * Provide a String representation of this file filter.
     *
     * @return a String representation
     *
     * Checker issues warning of null reference in method below. This is because
     * implementation below does not perform null check for filenameFilter.
     *
     * Both constructors ensure that either of the value for filefilter/filenameFilter
     * is non-null and hence no NPE occur at runtime.
     */
    @Override
    @Pure
    public String toString() {
        final String delegate = fileFilter != null ? fileFilter.toString() : filenameFilter.toString();
        return super.toString() + "(" + delegate + ")";
    }

}
