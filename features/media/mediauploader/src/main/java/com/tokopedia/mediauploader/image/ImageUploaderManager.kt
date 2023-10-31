package com.tokopedia.mediauploader.image

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.BaseParam
import com.tokopedia.mediauploader.ImageParam
import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.analytics.UploaderLogger
import com.tokopedia.mediauploader.common.data.consts.FILE_NOT_FOUND
import com.tokopedia.mediauploader.common.data.consts.SOURCE_NOT_FOUND
import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR
import com.tokopedia.mediauploader.common.data.consts.formatNotAllowedMessage
import com.tokopedia.mediauploader.common.data.consts.maxFileSizeMessage
import com.tokopedia.mediauploader.common.data.consts.maxResBitmapMessage
import com.tokopedia.mediauploader.common.data.consts.minResBitmapMessage
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isMaxBitmapResolution
import com.tokopedia.mediauploader.common.util.isMaxFileSize
import com.tokopedia.mediauploader.common.util.isMinBitmapResolution
import com.tokopedia.mediauploader.image.data.params.ImageUploadParam
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import java.io.File
import javax.inject.Inject

class ImageUploaderManager @Inject constructor(
    private val imageUploaderUseCase: GetImageUploaderUseCase,
) : UploaderManager {

    suspend operator fun invoke(param: ImageParam): UploadResult {
        val base = param.base as BaseParam
        if (base.sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        return validateError(base.policy, base.file) ?: run {
            setProgressUploader(base.progress)
            upload(base.policy, param)
        }
    }

    private suspend fun upload(policy: SourcePolicy, param: ImageParam): UploadResult {
        val base = param.base as BaseParam

        val upload = imageUploaderUseCase(
            ImageUploadParam(
                timeOut = policy.timeOut.orZero().toString(),
                hostUrl = policy.host,
                sourceId = base.sourceId,
                file = base.file,
                isSecure = param.isSecure,
                extraBody = param.extraBody,
                extraHeader = param.extraHeader
            )
        )

        val requestId = upload.header.requestId ?: ""

        val error = if (upload.header.messages.isNotEmpty()) {
            upload.header.messages.first()
        } else {
            UNKNOWN_ERROR
        }

        return if (upload.data != null && upload.header.isSuccess) {
            upload.data.let {
                UploadResult.Success(
                    fileUrl = it.fileUrl,
                    uploadId = it.uploadId
                )
            }
        } else {
            UploadResult.Error(
                message = error,
                requestId = requestId
            ).also {
                UploaderLogger.commonError(base.sourceId, it)
            }
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
                else -> null
            }
        }?: return UploadResult.Error(UNKNOWN_ERROR)
    }

    override fun setProgressUploader(progress: ProgressUploader?) {
        imageUploaderUseCase.progressUploader = progress
    }
}
