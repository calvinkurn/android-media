@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.tokopedia.media.common.util

import java.io.File

fun File?.getDirSize(): Long {
    return this?.walkTopDown()
        ?.filter { it.isFile }
        ?.map { it.length() }
        ?.sum()
        ?: 0L
}