package com.tokopedia.mediauploader.image

import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.common.data.consts.*
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.data.mapper.PolicyMapper
import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.fileExtension
import com.tokopedia.mediauploader.common.util.isMaxBitmapResolution
import com.tokopedia.mediauploader.common.util.isMaxFileSize
import com.tokopedia.mediauploader.common.util.isMinBitmapResolution
import com.tokopedia.mediauploader.image.data.params.ImageUploadParam
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import java.io.File
import javax.inject.Inject

class ImageUploaderManager @Inject constructor(
    private val imagePolicyUseCase: GetImagePolicyUseCase,
    private val imageUploaderUseCase: GetImageUploaderUseCase
) : UploaderManager {

    suspend operator fun invoke(file: File, sourceId: String): UploadResult {
        if (sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        val filePath = file.path
        val policyData = imagePolicyUseCase(sourceId)
        val sourcePolicy = PolicyMapper.map(policyData.dataPolicy)

        if (sourcePolicy.imagePolicy != null) {
            val extensions = sourcePolicy.imagePolicy.extension.split(",")
            val maxFileSize = sourcePolicy.imagePolicy.maxFileSize
            val maxRes = sourcePolicy.imagePolicy.maximumRes
            val minRes = sourcePolicy.imagePolicy.minimumRes

            return when {
                !file.exists() -> {
                    UploadResult.Error(FILE_NOT_FOUND)
                }
                file.isMaxFileSize(maxFileSize) -> {
                    UploadResult.Error(maxFileSizeMessage(maxFileSize))
                }
                !extensions.contains(filePath.fileExtension()) -> {
                    UploadResult.Error(formatNotAllowedMessage(sourcePolicy.imagePolicy.extension))
                }
                filePath.isMaxBitmapResolution(maxRes.width, maxRes.height) -> {
                    UploadResult.Error(maxResBitmapMessage(maxRes.width, maxRes.height))
                }
                filePath.isMinBitmapResolution(minRes.width, minRes.height) -> {
                    UploadResult.Error(minResBitmapMessage(minRes.width, minRes.height))
                }
                else -> {
                    upload(file, sourceId, sourcePolicy)
                }
            }
        } else {
            return UploadResult.Error(UNKNOWN_ERROR)
        }
    }

    private suspend fun upload(file: File, sourceId: String, policy: SourcePolicy): UploadResult {
        val upload = imageUploaderUseCase(ImageUploadParam(
            hostUrl = policy.host,
            sourceId = sourceId,
            file = file,
            timeOut = policy.timeOut.toString(),
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

    override fun setProgressUploader(progress: ProgressCallback?) {
        imageUploaderUseCase.progressCallback = progress
    }

}