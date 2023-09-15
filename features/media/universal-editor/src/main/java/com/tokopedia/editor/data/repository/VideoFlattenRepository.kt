package com.tokopedia.editor.data.repository

import android.graphics.Bitmap
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

typealias FlattenParam = VideoFlattenRepositoryImpl.Param

interface VideoFlattenRepository {

    fun flatten(param: FlattenParam): Flow<String>
}

class VideoFlattenRepositoryImpl @Inject constructor() : VideoFlattenRepository {

    override fun flatten(param: FlattenParam): Flow<String> {
        return callbackFlow {
            val bitmapToFile = convertCanvasTextBitmapToFilePath(param.canvasText)
            if (bitmapToFile.isEmpty()) trySend("")

            val command = createFfmpegParam(
                param.videoPath,
                bitmapToFile,
                param.isRemoveAudio
            )

            FFmpeg.executeAsync(command) { _, returnCode ->
                if (returnCode == Config.RETURN_CODE_SUCCESS) {
                    trySend(flattenResultFilePath())
                } else {
                    trySend("")
                }
            }

            awaitClose { channel.close() }
        }
    }

    private fun convertCanvasTextBitmapToFilePath(canvasText: Bitmap): String {
        return ImageProcessingUtil
            .writeImageToTkpdPath(canvasText, Bitmap.CompressFormat.PNG)
            ?.path ?: ""
    }

    private fun createFfmpegParam(
        videoPath: String,
        textPath: String,
        isRemoveAudio: Boolean
    ): String {
        val portraitSize = "[0:v]scale=1080:1920"
        val aspectRatioDisabled = "force_original_aspect_ratio=decrease"
        val blackCanvas = "pad=1080:1920:(ow-iw)/2:(oh-ih)/2[video];[video][1:v]overlay=0:0"

        val filter = "$portraitSize:$aspectRatioDisabled,$blackCanvas"
        val removeAudioCommand = if (isRemoveAudio) "-an" else ""

        return "-i $videoPath -i $textPath -filter_complex \"$filter\" -c:a copy -f mp4 $removeAudioCommand -y ${flattenResultFilePath()}"
    }

    private fun cacheDir() = FileUtil.getTokopediaInternalDirectory(CACHE_FOLDER).path

    private fun flattenResultFilePath() = cacheDir() + "final_result.mp4"

    data class Param(
        val videoPath: String,
        val canvasText: Bitmap,
        val isRemoveAudio: Boolean
    )

    companion object {
        private const val CACHE_FOLDER = "Tokopedia"
    }
}
