package com.tokopedia.topchat.common.util

import android.graphics.BitmapFactory
import java.io.File

object ImageUtil {

    private const val MAX_FILE_SIZE = 15360
    private const val MINIMUM_HEIGHT = 100
    private const val MINIMUM_WIDTH = 300
    private const val DEFAULT_ONE_MEGABYTE: Long = 1024

    const val IMAGE_NO_URI = 100
    const val IMAGE_UNDERSIZE = 101
    const val IMAGE_EXCEED_SIZE_LIMIT = 102
    const val IMAGE_VALID = 103

    fun validateImageAttachment(uri: String?): Pair<Boolean, Int> {
        if (uri == null) return Pair(false, IMAGE_NO_URI)
        val file = File(uri)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth

        val fileSize = Integer.parseInt((file.length() / DEFAULT_ONE_MEGABYTE).toString())

        return if (imageHeight < MINIMUM_HEIGHT || imageWidth < MINIMUM_WIDTH) {
            Pair(false, IMAGE_UNDERSIZE)
        } else if (fileSize >= MAX_FILE_SIZE) {
            Pair(false, IMAGE_EXCEED_SIZE_LIMIT)
        } else {
            Pair(true, IMAGE_VALID)
        }
    }
}