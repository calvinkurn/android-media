package com.tokopedia.editor.data.repository

import android.graphics.Bitmap
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.tokopedia.editor.analytics.EditorLogger
import com.tokopedia.editor.data.model.CanvasSize
import com.tokopedia.editor.util.getEditorCacheFolderPath
import com.tokopedia.utils.file.FileUtil
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

typealias FlattenParam = VideoFlattenRepositoryImpl.Param

interface VideoFlattenRepository {

    fun flatten(param: FlattenParam, fileNameAppendix: String): Flow<String>
    fun isFlattenOngoing(): Boolean
    fun cancel()
}

class VideoFlattenRepositoryImpl @Inject constructor(
    private val imageSaveRepository: ImageSaveRepository,
    private val metadataExtractorRepository: VideoExtractMetadataRepository
) : VideoFlattenRepository {

    private var executionId = 0L

    override fun flatten(param: FlattenParam, fileNameAppendix: String): Flow<String> {
        return callbackFlow {
            // this resized ratio will be used both canvas image and video output
            val metadata = metadataExtractorRepository.extract(param.videoPath)
            val canvasSize = newRes(metadata.width)

            // convert the bitmap from canvas layout into file
            val textCanvasPath = imageSaveRepository.saveBitmap(param.canvasTextBitmap, canvasSize)
            if (textCanvasPath.isEmpty()) trySend("")

            val outputPath = flattenResultFilePath(fileNameAppendix)

            val command = createFfmpegParam(
                textCanvasPath,
                param.videoPath,
                param.isRemoveAudio,
                canvasSize,
                outputPath
            )

            executionId = FFmpeg.executeAsync(command) { _, returnCode ->
                if (returnCode == Config.RETURN_CODE_SUCCESS) {
                    trySend(outputPath)
                } else {
                    trySend("")
                }

                Config.enableLogCallback {
                    EditorLogger.videoFlatten(returnCode, command, it.text)
                }
            }

            awaitClose { channel.close() }
        }
    }

    override fun isFlattenOngoing(): Boolean {
        return FFmpeg.listExecutions().isNotEmpty() && executionId != 0L
    }

    override fun cancel() {
        try {
            FFmpeg.cancel(executionId)
        } catch (ignored: Throwable) {}
    }

    private fun createFfmpegParam(
        textPath: String,
        videoPath: String,
        isRemoveAudio: Boolean,
        canvasSize: CanvasSize,
        outputPath: String
    ): String {
        val (width, height) = canvasSize

        val portraitSize = "[0:v]scale=$width:$height"
        val aspectRatioDisabled = "force_original_aspect_ratio=decrease"

        // quick (temp) solution: increase the pad size of width and height with 1px.
        // will address the flatten command of ffmpeg after merge.
        val blackCanvas = "pad=${width + 1}:${height + 1}:(ow-iw)/2:(oh-ih)/2[video];[video][1:v]overlay=0:0"

        val filter = "$portraitSize:$aspectRatioDisabled,$blackCanvas"
        val removeAudioCommand = if (isRemoveAudio) "-an" else ""

        return "-i \"$videoPath\" -i \"$textPath\" -filter_complex \"$filter\" -c:a copy -f mp4 $removeAudioCommand -y $outputPath"
    }

    private fun newRes(width: Int): CanvasSize {
        val scaledWidth = if (width > MAX_WIDTH) MAX_WIDTH else if (width < MIN_WIDTH) MIN_WIDTH else width
        val scaledHeight = ((scaledWidth.toDouble() / RATIO_WIDTH) * RATIO_HEIGHT).toInt()

        return CanvasSize(scaledWidth, scaledHeight)
    }

    private fun flattenResultFilePath(fileNameAppendix: String): String {
        return getEditorCacheFolderPath() + if (fileNameAppendix.isEmpty()) {
            // no appendix value, will reuse file to reduce size
            "$VIDEO_RESULT_BASE_FILENAME.mp4"
        } else {
            // with appendix value, will generate new file each export if appendix value is unique each export
            "${VIDEO_RESULT_BASE_FILENAME}_$fileNameAppendix.mp4"
        }
    }

    data class Param(
        val videoPath: String,
        val canvasTextBitmap: Bitmap,
        val isRemoveAudio: Boolean
    )

    companion object {
        private const val VIDEO_RESULT_BASE_FILENAME = "stories_editor_result"

        private const val MAX_WIDTH = 720
        private const val MIN_WIDTH = 480

        private const val RATIO_WIDTH = 9
        private const val RATIO_HEIGHT = 16
    }
}
