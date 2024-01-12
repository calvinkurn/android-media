package com.tokopedia.play_common.util

import android.content.Context
import android.graphics.Bitmap
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.view.getBitmapFromUrl
import com.tokopedia.utils.image.ImageProcessingUtil
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 15, 2022
 */
class VideoSnapshotHelper @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
) {

    private var tempFile: File? = null

    suspend fun snapVideo(
        context: Context,
        filePath: String,
    ): File? {
        return withContext(dispatchers.io) {
            val bitmap = snapVideoBitmap(context, filePath) ?: return@withContext null

            ImageProcessingUtil.writeImageToTkpdPath(bitmap, Bitmap.CompressFormat.JPEG).also { file ->
                tempFile = file
            }
        }
    }

    suspend fun snapVideoBitmap(
        context: Context,
        filePath: String,
    ): Bitmap? {
        return withContext(dispatchers.io) {
            context.getBitmapFromUrl(filePath)
        }
    }

    fun deleteLocalFile() {
        if (tempFile?.exists() == true) {
            tempFile?.delete()
        }
    }
}
