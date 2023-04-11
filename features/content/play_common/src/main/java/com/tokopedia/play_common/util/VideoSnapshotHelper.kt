package com.tokopedia.play_common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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
            val bitmap = Glide.with(context)
                .asBitmap()
                .load(filePath)
                .submit()
                .get()

            ImageProcessingUtil.writeImageToTkpdPath(bitmap, Bitmap.CompressFormat.JPEG).also { file ->
                tempFile = file
            }
        }
    }

    fun deleteLocalFile() {
        if (tempFile?.exists() == true) {
            tempFile?.delete()
        }
    }
}
