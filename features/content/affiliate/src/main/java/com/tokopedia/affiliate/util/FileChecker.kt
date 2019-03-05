package com.tokopedia.affiliate.util

import java.io.File

/**
 * @author by milhamj on 26/02/19.
 */

private const val FILE_PREFIX = "file:"

fun urlIsFile(input: String?): Boolean {
    if (input == null) return false
    if (input.startsWith(FILE_PREFIX)) return true
    return try {
        File(input).exists()
    } catch (e: Exception) {
        false
    }

}