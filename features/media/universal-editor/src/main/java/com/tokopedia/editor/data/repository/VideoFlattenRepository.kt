package com.tokopedia.editor.data.repository

import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.tokopedia.utils.file.FileUtil
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface VideoFlattenRepository {

    fun flatten(videoPath: String, canvasPath: String): Flow<String>
}

class VideoFlattenRepositoryImpl @Inject constructor() : VideoFlattenRepository {

    override fun flatten(videoPath: String, canvasPath: String): Flow<String> {
        return callbackFlow {
            val command = resizeAndFlattenVideoWithTextCommand(videoPath, canvasPath)

            FFmpeg.executeAsync(command) { _, returnCode ->
                if (returnCode == Config.RETURN_CODE_SUCCESS) {
                    trySend(flattenResultFilePath())
                } else {
                    trySend("")
                }
            }
        }

//        it works, but too slow.
//        return generateDefaultRatioVideo(videoPath)
//            .flatMapLatest {
//                flattenVideoWithImage(it, canvasPath)
//            }
    }

    private fun flattenVideoWithImage(videoPath: String, canvasPath: String) = callbackFlow {
        val cacheDir = FileUtil.getTokopediaInternalDirectory(CACHE_FOLDER).path
        val outputFilePath = cacheDir + FileUtil.generateUniqueFileName() + ".mp4"

        val command = flattenDefaultRatioVideoWithTextCanvas(videoPath, canvasPath, outputFilePath)
        FFmpeg.executeAsync(command) { _, returnCode ->
            if (returnCode == Config.RETURN_CODE_SUCCESS) {
                trySend(outputFilePath)
            } else {
                trySend("")
            }
        }

        awaitClose { channel.close() }
    }

    private fun generateDefaultRatioVideo(videoPath: String) = callbackFlow {
        // TODO: to reduce computation, let's add the vod dimensions, don't generate if the video is already 16:9

        val command = resizeVideoIntoDefaultRatioCommand(videoPath)
        FFmpeg.executeAsync(command) { _, returnCode ->
            if (returnCode == Config.RETURN_CODE_SUCCESS) {
                trySend(flattenResultFilePath())
            } else {
                trySend("")
            }
        }

        awaitClose { channel.close() }
    }

    private fun resizeVideoIntoDefaultRatioCommand(videoPath: String) =
        "-i $videoPath -vf \"scale=1080:1920:force_original_aspect_ratio=decrease,pad=1080:1920:(ow-iw)/2:(oh-ih)/2\" -c:a copy -f mp4 -y ${flattenResultFilePath()}"

    private fun flattenDefaultRatioVideoWithTextCanvas(videoPath: String, canvasPath: String, outputPath: String) =
        "-i $videoPath -i $canvasPath -filter_complex \"[0:v]scale=1080:1920[video];[video][1:v]overlay=0:0\" -c:a copy -f mp4 -y $outputPath"

    private fun resizeAndFlattenVideoWithTextCommand(videoPath: String, textPath: String) =
        "-i $videoPath -i $textPath -filter_complex \"[0:v]scale=1080:1920:force_original_aspect_ratio=decrease,pad=1080:1920:(ow-iw)/2:(oh-ih)/2[video];[video][1:v]overlay=0:0\" -c:a copy -f mp4 -y ${flattenResultFilePath()}"

    private fun cacheDir() = FileUtil.getTokopediaInternalDirectory(CACHE_FOLDER).path

    private fun flattenResultFilePath() = cacheDir() + "temporary.mp4"

    companion object {
        private const val CACHE_FOLDER = "Tokopedia/flatten/"
    }
}
