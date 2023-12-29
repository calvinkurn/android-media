package com.tokopedia.mediauploader.image

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.BaseParam
import com.tokopedia.mediauploader.BaseUploaderParam
import com.tokopedia.mediauploader.ImageParam
import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.analytics.UploaderLogger
import com.tokopedia.mediauploader.common.cache.SourcePolicyManager
import com.tokopedia.mediauploader.common.data.consts.SOURCE_NOT_FOUND
import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.image.data.params.ImageUploadParam
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import javax.inject.Inject

class ImageUploaderManager @Inject constructor(
    private val imageUploaderUseCase: GetImageUploaderUseCase,
    @UploaderQualifier val sourcePolicyManager: SourcePolicyManager
) : UploaderManager {

    override suspend fun upload(param: BaseUploaderParam): UploadResult {
        val base = (param as ImageParam).base as BaseParam
        val policy = sourcePolicyManager.get() ?: return UploadResult.Error(UNKNOWN_ERROR)
        if (base.sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        val (isValid, message) = ImageUploaderValidator(base.file, policy.imagePolicy)
        if (isValid.not()) return UploadResult.Error(message)

        setProgressUploader(base.progress)
        return upload(policy, param)
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
                UploaderLogger.commonError(base, it)
            }
        }
    }

    override fun setProgressUploader(progress: ProgressUploader?) {
        imageUploaderUseCase.progressUploader = progress
    }
}
