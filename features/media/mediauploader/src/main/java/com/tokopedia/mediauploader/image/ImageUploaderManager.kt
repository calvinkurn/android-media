package com.tokopedia.mediauploader.image

import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.common.data.consts.*
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.internal.SourcePolicyManager
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isMaxBitmapResolution
import com.tokopedia.mediauploader.common.util.isMaxFileSize
import com.tokopedia.mediauploader.common.util.isMinBitmapResolution
import com.tokopedia.mediauploader.image.data.params.ImageUploadParam
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import java.io.File
import javax.inject.Inject

class ImageUploaderManager @Inject constructor(
    private val policyManager: SourcePolicyManager,
    private val imagePolicyUseCase: GetImagePolicyUseCase,
    private val imageUploaderUseCase: GetImageUploaderUseCase
) : UploaderManager {

    suspend operator fun invoke(file: File, sourceId: String, loader: ProgressUploader?): UploadResult {
        if (sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        // hit the uploader policy
        val policy = imagePolicyUseCase(sourceId)
        policyManager.set(policy)

        // return the upload result
        return policy.imagePolicy?.let { imagePolicy ->
            val maxFileSize = imagePolicy.maxFileSize
            val maxRes = imagePolicy.maximumRes
            val minRes = imagePolicy.minimumRes
            val filePath = file.path

            when {
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
                    setProgressUploader(loader)
                    upload(file, sourceId, policy)
                }
            }
        }?: UploadResult.Error(UNKNOWN_ERROR)
    }

    private suspend fun upload(file: File, sourceId: String, policy: SourcePolicy): UploadResult {
        val upload = imageUploaderUseCase(ImageUploadParam(
            timeOut = policy.timeOutString(),
            hostUrl = policy.host,
            sourceId = sourceId,
            file = file,
        ))

        val error = if (upload.header.messages.isNotEmpty()) {
            upload.header.messages.first()
        } else {
            UNKNOWN_ERROR
        }

        return upload.data?.let {
            UploadResult.Success(uploadId = it.uploadId)
        }?: UploadResult.Error(error)
    }

    override fun setProgressUploader(progress: ProgressUploader?) {
        imageUploaderUseCase.progressUploader = progress
    }

}