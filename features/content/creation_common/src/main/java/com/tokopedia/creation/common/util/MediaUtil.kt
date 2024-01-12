package com.tokopedia.creation.common.util

import android.graphics.BitmapFactory
import java.io.File

/**
 * Created By : Jonathan Darwin on October 30, 2023
 */

fun getMediaSize(filePath: String): Pair<Int, Int> {
    return try {
        val options = BitmapFactory.Options()
        BitmapFactory.decodeFile(File(filePath).absolutePath, options)
        val width = options.outWidth
        val height = options.outHeight

        width to height
    } catch(_: Throwable) {
        0 to 0
    }
}

fun isMediaPotrait(filePath: String): Boolean {
    val (width, height) = getMediaSize(filePath)
    return width <= height
}

fun isMediaLandscape(filePath: String): Boolean {
    return !isMediaPotrait(filePath)
}

fun isMediaRatioSame(filePath: String, ratio: Float): Boolean {
    val (width, height) = getMediaSize(filePath)

    if (height == 0) return false

    return ratio == (width / height.toFloat())
}

const val RATIO_9_16 = 9 / 16.toFloat()
