package com.tokopedia.liveness.utils

import ai.advance.liveness.lib.Detector
import ai.advance.liveness.lib.Detector.DetectionType.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import timber.log.Timber
import java.io.File

object LivenessDetectionLogTracker {

    enum class LogType {
        SUCCESS,
        FAILED,
        LIBRARY
    }

    fun sendLog(
        logType: LogType,
        clazz: String,
        detectionType: Detector.DetectionType? = NONE,
        actionStatus: Detector.ActionStatus? = null,
        warnCode: Detector.WarnCode? = null,
        detectionFailedType: Detector.DetectionFailedType? = null,
        message: String? = null,
        throwable: Throwable? = null
    ) {
        ServerLogger.log(
            Priority.P2, "LIVENESS_DETECTION", mapOf(
                "type" to logType.name,
                "class" to clazz,
                "detectionType" to detectionType?.name.orEmpty(),
                "actionStatus" to actionStatus?.name.orEmpty(),
                "warnCode" to warnCode?.name.orEmpty(),
                "detectionFailedType" to detectionFailedType?.name.orEmpty(),
                "message" to message.orEmpty(),
                "stackTrace" to throwable?.message.orEmpty()
            )
        )

        Timber.d(throwable, message)
    }

    enum class ImageFailedProcessType {
        FailedImageFileNotFound,
        TryCatchSaveToFile,
        TryCatchWriteImageToTkpdPath
    }

    fun sendLogImageProcess(
        type: ImageFailedProcessType,
        throwable: Throwable,
        cachePath: String? = null,
        file: File? = null
    ) {
        ServerLogger.log(
            Priority.P2, "LIVENESS_IMAGE_ERROR", mapOf(
                "type" to type.name,
                "cachePath" to cachePath.orEmpty(),
                "fileExists" to "${file?.exists() == true}",
                "stack_trace" to "${throwable.message}"
            )
        )

        Timber.d(throwable)
    }
}