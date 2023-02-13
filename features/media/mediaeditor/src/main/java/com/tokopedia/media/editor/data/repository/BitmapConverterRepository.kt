package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

interface BitmapConverterRepository {
    fun uriToBitmap(uri: Uri): Bitmap?
}

class BitmapConverterRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BitmapConverterRepository {

    override fun uriToBitmap(uri: Uri): Bitmap? {
        return Glide.with(context)
            .asBitmap()
            .load(uri.path)
            .submit()
            .get()
    }

}
