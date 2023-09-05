package com.tokopedia.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.yalantis.ucrop.util.BitmapLoadUtils
import java.io.File
import java.io.IOException
import java.io.OutputStream
import javax.inject.Inject
import kotlin.jvm.Throws

interface ImageSaveRepository {
    @Throws(IOException::class)
    fun saveBitmap(outputPath: String, bitmap: Bitmap): String
}

class ImageSaveRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): ImageSaveRepository{

    override fun saveBitmap(outputPath: String, bitmap: Bitmap): String {
        var outputStream: OutputStream? = null
        try {
            outputStream = context.contentResolver.openOutputStream(Uri.fromFile(File(outputPath)))
            bitmap.compress(Bitmap.CompressFormat.PNG,
                IMAGE_QUALITY, outputStream)
            bitmap.recycle()
        } catch (e: Exception) {
            throw IOException(e.message)
        } finally {
            BitmapLoadUtils.close(outputStream)
        }

        return outputPath
    }

    companion object {
        private const val IMAGE_QUALITY = 100
    }
}
