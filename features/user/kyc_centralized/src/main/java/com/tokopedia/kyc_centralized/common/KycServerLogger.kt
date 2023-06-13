package com.tokopedia.kyc_centralized.common

import android.graphics.Bitmap
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.utils.file.FileUtil
import java.io.ByteArrayOutputStream

object KycServerLogger {

    /**
     * ## Centralize Server Logger for KYC Feature
     * */

    private const val LIMIT_MESSAGE = 1000
    private const val MB_DIVIDER_FLOAT = 1024F

    fun kycUploadMonitoring(
        type: String,
        uploadTime: Long,
        encryptionTimeFileKtp: Long = 0L,
        encryptionTimeFileFace: Long = 0L,
        fileKtp: String = "",
        fileFace: String = "",
        message: String = ""
    ) {
        ServerLogger.log(
            Priority.P2, "KYC_UPLOAD_MONITORING", mapOf(
                "status" to type,
                "uploadTime" to "${uploadTime}ms",
                "encryptionTimeFileKtp" to "${encryptionTimeFileKtp}ms",
                "encryptionTimeFileFace" to "${encryptionTimeFileFace}ms",
                "ktpFileSize" to if (fileKtp.isNotEmpty()) "${FileUtil.getFileSizeInKb(fileKtp)}Kb" else "-",
                "faceFileSize" to if (fileFace.isNotEmpty()) "${FileUtil.getFileSizeInKb(fileFace)}Kb" else "-",
                "message" to message.take(LIMIT_MESSAGE)
            )
        )
    }

    fun livenessUploadResult(
        status: String,
        ktpFile: String,
        faceFile: String,
        projectId: String,
        throwable: Throwable
    ) {
        ServerLogger.log(
            Priority.P2, "LIVENESS_UPLOAD_RESULT", mapOf(
                "status" to status, // before use `type`
                "ktpPath" to ktpFile,
                "facePath" to faceFile,
                "tkpdProjectId" to projectId,
                "stack_trace" to throwable.message?.take(LIMIT_MESSAGE).orEmpty()
            )
        )
    }

    fun selfieUploadResult(
        status: String,
        ktpFile: String,
        faceFile: String,
        projectId: String,
        throwable: Throwable
    ) {
        ServerLogger.log(
            Priority.P2, "SELFIE_UPLOAD_RESULT", mapOf(
                "status" to status, // before use `type`
                "ktpPath" to ktpFile,
                "facePath" to faceFile,
                "tkpdProjectId" to projectId,
                "stack_trace" to throwable.toString()
            )
        )
    }

    fun kyUploadResult(
        status: String,
        ktpFile: String,
        faceFile: String,
        projectId: String,
        isKycSelfie: Boolean
    ) {
        ServerLogger.log(
            Priority.P2, "SELFIE_UPLOAD_RESULT", mapOf(
                "status" to status, // before use `type`
                "ktpPath" to ktpFile,
                "facePath" to faceFile,
                "tkpdProjectId" to projectId,
                "method" to if (isKycSelfie) "selfie" else "liveness",
            )
        )
    }

    fun kycCropAndCompression(
        isSuccess: Boolean,
        bitmapOriginal: Bitmap? = null,
        bitmapCropped: Bitmap? = null,
        bitmapCompressed: Bitmap? = null,
        bitmapFinal: Bitmap? = null,
        croppingTimeProcess: Long? = 0L,
        compressionTimeProcess: Long? = 0L,
        compressionQuality: Float = 0F,
        throwable: Throwable? = null
    ) {
        val log = mapOf(
            "status" to if (isSuccess) "Success" else "Failed",
            "originalSizeInKb" to "${bitmapOriginal?.let { calculateSize(it) }}",
            "originalResolution" to "${bitmapOriginal?.width}x${bitmapOriginal?.height}",
            "croppedSizeInKb" to "${bitmapCropped?.let { calculateSize(it) }}",
            "croppedResolution" to "${bitmapCropped?.width}x${bitmapCropped?.height}",
            "compressedSizeInKb" to "${bitmapCompressed?.let { calculateSize(it) }}",
            "compressedResolution" to "${bitmapCompressed?.width}x${bitmapCompressed?.height}",
            "compressionQualityPercentage" to compressionQuality.toString(),
            "finalResolution" to "${bitmapFinal?.width}x${bitmapFinal?.height}",
            "croppingTimeProcessInMs" to "$croppingTimeProcess",
            "compressionTimeProcessInMs" to "$compressionTimeProcess",
            "processTimeInMs" to "${croppingTimeProcess.orZero() + compressionTimeProcess.orZero()}",
            "stackTrace" to throwable?.message.toString()
        )

        ServerLogger.log(Priority.P2, "KYC_CROP_AND_COMPRESSION", log)
    }

    /**
     * @return size in MB
     */
    private fun calculateSize(bitmap: Bitmap): Float {
        return try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val result = stream.size().toFloat() / MB_DIVIDER_FLOAT
            stream.flush()
            stream.close()
            result
        } catch (e: Exception) {
            0F
        }
    }
}
