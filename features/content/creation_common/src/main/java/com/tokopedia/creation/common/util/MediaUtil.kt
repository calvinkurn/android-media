package com.tokopedia.creation.common.util

import android.graphics.BitmapFactory
import java.io.File

/**
 * Created By : Jonathan Darwin on October 30, 2023
 */

fun getMediaSize(filePath: String): Pair<Int, Int> {
    val options = BitmapFactory.Options()
    BitmapFactory.decodeFile(File(filePath).absolutePath, options)
    val width = options.outWidth
    val height = options.outHeight

    return width to height
}

fun isMediaPotrait(filePath: String): Boolean {
    val (width, height) = getMediaSize(filePath)
    return width < height
}

fun isMediaLandscape(filePath: String): Boolean {
    return !isMediaPotrait(filePath)
}

fun isMediaRatioSame(filePath: String, ratio: Float): Boolean {
    val (width, height) = getMediaSize(filePath)
    return ratio == (width / height.toFloat())
}

const val RATIO_9_16 = 9 / 16.toFloat()
