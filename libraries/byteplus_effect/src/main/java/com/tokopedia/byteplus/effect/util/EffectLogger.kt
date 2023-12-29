package com.tokopedia.byteplus.effect.util

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

/**
 * Created By : Jonathan Darwin on June 09, 2023
 */
object EffectLogger {

    fun sendErrorCreateDir(dir: String) {
        sendLog(
            mapOf(
                ERROR_MESSAGE to "failed to mkdir",
                DIR_PATH to dir,
            )
        )
    }

    fun sendErrorUnzip(e: Exception, dirPath: String) {
        sendLog(
            mapOf(
                ERROR_MESSAGE to (e.message ?: "fail to unzip file"),
                DIR_PATH to dirPath,
            )
        )
    }

    fun sendErrorWriteToDisk(e: Exception, dirPath: String, fileName: String) {
        sendLog(
            mapOf(
                ERROR_MESSAGE to (e.message ?: "fail to write to disk"),
                DIR_PATH to dirPath,
                FILE_NAME to fileName,
            )
        )
    }

    private fun sendLog(message: Map<String, String>) {
        ServerLogger.log(Priority.P2, TAG_BYTEPLUS_EFFECT, message)
    }

    private const val TAG_BYTEPLUS_EFFECT = "BYTEPLUS_EFFECT"

    private const val ERROR_MESSAGE = "error_message"
    private const val DIR_PATH = "dir_path"
    private const val FILE_NAME = "file_name"
}
