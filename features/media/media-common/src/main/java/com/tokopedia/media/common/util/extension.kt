@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.tokopedia.media.common.util

import java.io.File

fun getDirSize(file: File?): Long {
    var dirSize = 0L

    if (file == null) return dirSize

    for (_file: File in file.listFiles()) {
        if (_file.isDirectory) {
            dirSize += getDirSize(_file)
        } else if (file.isFile) {
            dirSize += file.length()
        }
    }

    return dirSize
}