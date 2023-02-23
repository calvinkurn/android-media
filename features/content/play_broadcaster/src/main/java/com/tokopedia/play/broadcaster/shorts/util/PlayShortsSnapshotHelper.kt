package com.tokopedia.play.broadcaster.shorts.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 15, 2022
 */
class PlayShortsSnapshotHelper @Inject constructor() {

    private var tempFile: File? = null

    fun snap(
        context: Context,
        filePath: String,
        onBitmapReady: (tempFile: File) -> Unit
    ) {
        Glide.with(context)
            .asBitmap()
            .load(filePath)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    saveTemporaryBitmap(resource)
                    tempFile?.let(onBitmapReady)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun saveTemporaryBitmap(bitmap: Bitmap) {
        tempFile = ImageProcessingUtil.writeImageToTkpdPath(bitmap, Bitmap.CompressFormat.JPEG)
    }

    fun deleteLocalFile() {
        if (tempFile?.exists() == true) {
            tempFile?.delete()
        }
    }
}
