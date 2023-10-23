package com.tokopedia.editor.analytics

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import java.io.File

object EditorLogger {

    private const val TAG = "MEDIA_UNIVERSAL_EDITOR"

    /**
     * A log during convert bitmap into file.
     *
     * @output:
     * {
     *    "filename": "sample.png",
     *    "err": "stacktrace from throwable"
     * }
     */
    fun saveBitmap(path: String, message: String) = send(
        type = LoggerType.SaveBitmap,
        message = mapOf(
            "filename" to File(path).name,
            "err" to message,
        )
    )

    /**
     * A log during extract meta data of video file.
     *
     * @output:
     * {
     *    "err": "stacktrace from throwable"
     * }
     */
    fun videoExtractMetadata(message: String) = send(
        type = LoggerType.ExtractMetadata,
        message = mapOf(
            "err" to message
        )
    )

    /**
     * A log during flatten out a video using FFMPEG.
     *
     * @output:
     * {
     *    "return_code": 255,
     *    "ffmpeg_command": "-i video.mp4 -filter_complex -c:a copy -f mp4 -y output.mp4",
     *    "message": "last log from ffmpeg"
     * }
     */
    fun videoFlatten(returnCode: Int, command: String, message: String) = send(
        type = LoggerType.VideoFlatten,
        message = mapOf(
            "return_code" to returnCode.toString(),
            "ffmpeg_command" to command,
            "message" to message
        )
    )

    private fun send(type: LoggerType, message: Map<String, String> = mapOf()) {
        ServerLogger.log(
            Priority.P2,
            TAG,
            mutableMapOf("type" to type.value).also {
                if (message.isNotEmpty()) {
                    it.putAll(message)
                }
            }
        )
    }

    sealed class LoggerType(val value: String) {
        object VideoFlatten : LoggerType("video_flatten")
        object ExtractMetadata : LoggerType("extract_metadata")
        object SaveBitmap : LoggerType("save_bitmap")
    }
}
