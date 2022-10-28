package com.tokopedia.liveness.utils

import ai.advance.liveness.lib.Detector
import ai.advance.liveness.lib.Detector.DetectionType.NONE
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import timber.log.Timber
import java.io.File

object LivenessDetectionLogTracker {

    private const val LIMIT_MESSAGE = 1000

    enum class LogType(val type: String) {
        SUCCESS("SUCCESS"),
        FAILED("FAILED"),
        LIBRARY("LIBRARY");

        override fun toString(): String {
            return type
        }
    }

    fun sendLog(
        logType: LogType,
        clazz: String,
        detectionType: Detector.DetectionType? = NONE,
        detectionFailedType: Detector.DetectionFailedType? = null,
        message: String? = null,
        throwable: Throwable? = null
    ) {
        ServerLogger.log(
            Priority.P2, "LIVENESS_DETECTION", mapOf(
                "detectionLogType" to logType.toString(), // before use `type`
                "class" to clazz,
                "detectionType" to detectionType?.name.orEmpty(),
                "detectionFailedType" to detectionFailedType?.name.orEmpty(),
                "message" to message.orEmpty(),
                "stackTrace" to throwable?.message?.take(LIMIT_MESSAGE).orEmpty()
            )
        )

        Timber.d(throwable, message)
    }

    enum class ImageFailedProcessType(val type: String) {
        FailedImageFileNotFound("FailedImageFileNotFound"),
        TryCatchSaveToFile("TryCatchSaveToFile"),
        TryCatchWriteImageToTkpdPath("TryCatchWriteImageToTkpdPath");

        override fun toString(): String {
            return type
        }
    }

    fun sendLogImageProcess(
        type: ImageFailedProcessType,
        throwable: Throwable,
        cachePath: String? = null,
        file: File? = null
    ) {
        ServerLogger.log(
            Priority.P2, "LIVENESS_IMAGE_ERROR", mapOf(
                "errorImageType" to type.toString(), // before use `type`
                "cachePath" to cachePath.orEmpty(),
                "fileExists" to "${file?.exists() == true}",
                "stack_trace" to throwable.message?.take(LIMIT_MESSAGE).orEmpty()
            )
        )

        Timber.d(throwable)
    }
}
