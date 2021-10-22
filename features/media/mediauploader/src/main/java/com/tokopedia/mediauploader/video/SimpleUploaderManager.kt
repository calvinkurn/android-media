package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.common.data.consts.FILE_NOT_FOUND
import com.tokopedia.mediauploader.common.data.consts.SOURCE_NOT_FOUND
import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR
import com.tokopedia.mediauploader.common.data.consts.formatNotAllowedMessage
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.fileExtension
import com.tokopedia.mediauploader.image.data.mapper.ImagePolicyMapper
import com.tokopedia.mediauploader.video.data.params.SimpleUploadParam
import com.tokopedia.mediauploader.video.domain.GetSimpleUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
import java.io.File

class SimpleUploaderManager constructor(
    private val policyUseCase: GetVideoPolicyUseCase,
    private val simpleUploaderUseCase: GetSimpleUploaderUseCase
) : UploaderManager {

    suspend operator fun invoke(file: File, sourceId: String): UploadResult {
        // sourceId empty validation
        if (sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        // file full path
        val filePath = file.path

        // request policy by sourceId
        val policyData = policyUseCase(sourceId)

        val sourcePolicy = ImagePolicyMapper.mapToSourcePolicy(
            policyData.dataPolicy
        )

        val onError = if (sourcePolicy.videoPolicy != null) {
            // get acceptable extension based on policy
            val extensions = sourcePolicy.videoPolicy.extension.split(",")

            setError(listOf(
                when {
                    !file.exists() -> FILE_NOT_FOUND
                    !extensions.contains(filePath.fileExtension()) ->
                        formatNotAllowedMessage(sourcePolicy.videoPolicy.extension)
                    else -> ""
                }
            ), sourceId, file) as UploadResult.Error
        } else {
            UploadResult.Error(UNKNOWN_ERROR)
        }

        return if (onError.message.isNotEmpty()) {
            onError
        } else {
            upload(file, sourceId)
        }
    }

    private suspend fun upload(file: File, sourceId: String): UploadResult {
        val uploader = simpleUploaderUseCase(
            SimpleUploadParam(
                sourceId = sourceId,
                file = file,
                timeOut = "120" // mock
            )
        )

        val error = if (!uploader.errorMessage.isNullOrBlank()) {
            listOf(uploader.errorMessage)
        } else {
            // error handling, when server returned empty error message
            listOf(UNKNOWN_ERROR)
        }

        return if (!uploader.videoUrl.isNullOrBlank()) {
            UploadResult.Success(videoUrl = uploader.videoUrl)
        } else {
            setError(error, sourceId, file)
        }
    }

}