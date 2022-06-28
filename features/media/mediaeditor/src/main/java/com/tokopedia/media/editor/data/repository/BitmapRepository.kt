package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

interface BitmapRepository {
    fun uriToBitmap(uri: Uri): Bitmap
}

class BitmapRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BitmapRepository {

    override fun uriToBitmap(uri: Uri): Bitmap {
        return try {
            val descriptor = context.contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor = descriptor?.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            descriptor?.close()

            image
        } catch (t: Throwable) {
            throw t
        }
    }

}