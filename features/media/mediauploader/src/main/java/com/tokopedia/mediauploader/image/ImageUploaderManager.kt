package com.tokopedia.mediauploader.image

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.common.data.consts.*
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.internal.SourcePolicyManager
import com.tokopedia.mediauploader.common.logger.DebugLog
import com.tokopedia.mediauploader.common.logger.onShowDebugLogcat
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isMaxBitmapResolution
import com.tokopedia.mediauploader.common.util.isMaxFileSize
import com.tokopedia.mediauploader.common.util.isMinBitmapResolution
import com.tokopedia.mediauploader.image.data.params.ImageUploadParam
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageSecurePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import java.io.File
import javax.inject.Inject

class ImageUploaderManager @Inject constructor(
    private val policyManager: SourcePolicyManager,
    private val imagePolicyUseCase: GetImagePolicyUseCase,
    private val imageUploaderUseCase: GetImageUploaderUseCase,
    private val imageSecurePolicyUseCase: GetImageSecurePolicyUseCase
) : UploaderManager {

    suspend operator fun invoke(
        file: File,
        sourceId: String,
        loader: ProgressUploader?,
        isSecure: Boolean = false,
        extraHeader: Map<String, String>,
        extraBody: Map<String, String>
    ): UploadResult {
        if (sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        // hit the uploader policy
        val policy = if (isSecure) imageSecurePolicyUseCase(sourceId) else imagePolicyUseCase(sourceId)
        policyManager.set(policy)

        return validateError(policy, file) ?: kotlin.run {
            setProgressUploader(loader)
            upload(file, sourceId, policy, isSecure, extraHeader, extraBody)
        }
    }

    private suspend fun upload(
        file: File,
        sourceId: String,
        policy: SourcePolicy,
        isSecure: Boolean,
        extraHeader: Map<String, String> = mapOf(),
        extraBody: Map<String, String> = mapOf()
    ): UploadResult {
        val upload = imageUploaderUseCase(ImageUploadParam(
            timeOut = policy.timeOut.orZero().toString(),
            hostUrl = policy.host,
            sourceId = sourceId,
            file = file,
            isSecure = isSecure,
            extraBody = extraBody,
            extraHeader = extraHeader
        ))

        val error = if (upload.header.messages.isNotEmpty()) {
            upload.header.messages.first()
        } else {
            UNKNOWN_ERROR
        }

        onShowDebugLogcat(
            DebugLog(
                sourceId = sourceId,
                sourceFile = file.path,
                uploadId = upload.data?.uploadId.toString(),
                sourcePolicy = policy
            )
        )

        return if (upload.data != null && upload.header.isSuccess) {
            upload.data.let {
                UploadResult.Success(fileUrl = it.fileUrl, uploadId = it.uploadId)
            }
        } else {
            UploadResult.Error(error)
        }
    }


    private fun validateError(policy: SourcePolicy, file: File): UploadResult? {
        policy.imagePolicy?.let { imagePolicy ->
            val maxFileSize = imagePolicy.maxFileSize
            val maxRes = imagePolicy.maximumRes
            val minRes = imagePolicy.minimumRes
            val filePath = file.path

            return when {
                !file.exists() -> {
                    UploadResult.Error(FILE_NOT_FOUND)
                }
                file.isMaxFileSize(maxFileSize) -> {
                    UploadResult.Error(maxFileSizeMessage(maxFileSize))
                }
                !allowedExt(filePath, imagePolicy.extension) -> {
                    UploadResult.Error(formatNotAllowedMessage(imagePolicy.extension))
                }
                filePath.isMaxBitmapResolution(maxRes.width, maxRes.height) -> {
                    UploadResult.Error(maxResBitmapMessage(maxRes.width, maxRes.height))
                }
                filePath.isMinBitmapResolution(minRes.width, minRes.height) -> {
                    UploadResult.Error(minResBitmapMessage(minRes.width, minRes.height))
                }
                else -> {
                    null
                }
            }
        }?: return UploadResult.Error(UNKNOWN_ERROR)
    }

    override fun setProgressUploader(progress: ProgressUploader?) {
        imageUploaderUseCase.progressUploader = progress
    }

}
