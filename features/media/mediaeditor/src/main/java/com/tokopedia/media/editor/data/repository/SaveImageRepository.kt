package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toFile
import com.tokopedia.media.editor.utils.getDestinationUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

interface SaveImageRepository {
    fun saveToCache(
        context: Context,
        bitmapParam: Bitmap,
        filename: String? = null
    ): File?
}

class SaveImageRepositoryImpl @Inject constructor() : SaveImageRepository {
    override fun saveToCache(
        context: Context,
        bitmapParam: Bitmap,
        filename: String?
    ): File? {
        return try {
            val file = getDestinationUri(context, filename).toFile()
            file.createNewFile()

            val bos = ByteArrayOutputStream()
            bitmapParam.compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapData = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()

            file
        } catch (e: Exception) {
            null
        }
    }
}