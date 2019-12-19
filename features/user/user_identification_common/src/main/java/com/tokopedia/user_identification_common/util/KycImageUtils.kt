package com.tokopedia.user_identification_common.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Created by Yoris Prayogo on 2019-12-19.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */
class KycImageUtils {
    companion object {

        const val MSG_FAIL_RESIZE_IMG = "Gagal memproses gambar. Silahkan foto ulang."
        fun resizeImage(imagePath: String, maxWidth: Int, maxHeight: Int): String {
            try {
                var image = BitmapFactory.decodeFile(imagePath)
                val width = image.width
                val height = image.height
                val ratioBitmap = width.toFloat() / height.toFloat()
                val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

                var finalWidth = maxWidth
                var finalHeight = maxHeight
                if (ratioMax > ratioBitmap) {
                    finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
                } else {
                    finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
                }

                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
                val baos = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b = baos.toByteArray()
                return Base64.encodeToString(b, Base64.DEFAULT)
            }catch (e: Exception){
                return ""
            }
        }
    }
}