package com.tokopedia.play.broadcaster.shorts.util

import android.content.Context
import android.graphics.Bitmap
import com.tokopedia.media.loader.getBitmapImageUrl
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
        filePath.getBitmapImageUrl(context) {
            saveTemporaryBitmap(it)
            tempFile?.let(onBitmapReady)
        }
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
