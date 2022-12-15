package com.tokopedia.media.picker.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
import javax.inject.Inject

interface BitmapConverterRepository {
    suspend fun convert(url: String): String?
}

class BitmapConverterRepositoryImpl @Inject constructor(
    private val context: Context
) : BitmapConverterRepository {

    override suspend fun convert(url: String): String? {
        // 1. convert image to bitmap
        val bitmap = urlToBitmap(url)?: return null

        // 2. bitmap to file
        val file = ImageProcessingUtil.writeImageToTkpdPath(
            bitmap,
            Bitmap.CompressFormat.JPEG
        )

        // 3. get file url
        return file?.path
    }

    private fun urlToBitmap(url: String): Bitmap? {
        return Glide.with(context)
            .asBitmap()
            .load(url)
            .submit()
            .get()
    }

}
