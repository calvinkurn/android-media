package com.tokopedia.mediauploader.image

import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.common.data.consts.*
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.fileExtension
import com.tokopedia.mediauploader.common.util.isMaxBitmapResolution
import com.tokopedia.mediauploader.common.util.isMaxFileSize
import com.tokopedia.mediauploader.common.util.isMinBitmapResolution
import com.tokopedia.mediauploader.image.data.mapper.ImagePolicyMapper
import com.tokopedia.mediauploader.image.data.params.ImageUploadParam
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import java.io.File

class ImageUploaderManager constructor(
    private val imagePolicyUseCase: GetImagePolicyUseCase,
    private val imageUploaderUseCase: GetImageUploaderUseCase
) : UploaderManager {

    suspend operator fun invoke(file: File, sourceId: String): UploadResult {
        // sourceId empty validation
        if (sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        // file full path
        val filePath = file.path

        // request policy by sourceId
        val policyData = imagePolicyUseCase(sourceId)

        val sourcePolicy = ImagePolicyMapper.mapToSourcePolicy(
            policyData.dataPolicy
        )

        val onError = if (sourcePolicy.imagePolicy != null) {
            // get acceptable extension based on policy
            val extensions = sourcePolicy.imagePolicy.extension.split(",")

            // get maximum file size
            val maxFileSize = sourcePolicy.imagePolicy.maxFileSize

            // get max and min bitmap resolution
            val maxRes = sourcePolicy.imagePolicy.maximumRes
            val minRes = sourcePolicy.imagePolicy.minimumRes

            setError(listOf(
                when {
                    !file.exists() -> FILE_NOT_FOUND
                    !extensions.contains(filePath.fileExtension()) ->
                        formatNotAllowedMessage(sourcePolicy.imagePolicy.extension)
                    file.isMaxFileSize(maxFileSize) ->
                        maxFileSizeMessage(maxFileSize)
                    filePath.isMaxBitmapResolution(maxRes.width, maxRes.height) ->
                        maxResBitmapMessage(maxRes.width, maxRes.height)
                    filePath.isMinBitmapResolution(minRes.width, minRes.height) ->
                        minResBitmapMessage(minRes.width, minRes.height)
                    else -> ""
                }
            ), sourceId, file) as UploadResult.Error
        } else {
            UploadResult.Error(UNKNOWN_ERROR)
        }

        return if (onError.message.isNotEmpty()) {
            onError
        } else {
            upload(file, sourceId, sourcePolicy)
        }
    }

    private suspend fun upload(file: File, sourceId: String, policy: SourcePolicy): UploadResult {
        // media uploader
        val uploaderParams = ImageUploadParam(
            hostUrl = policy.host,
            sourceId = sourceId,
            file = file,
            timeOut = policy.timeOut.toString(),
        )

        // upload file
        val upload = imageUploaderUseCase(uploaderParams)

        // getting error from gql response
        val error = if (upload.header.messages.isNotEmpty()) {
            upload.header.messages
        } else {
            // error handling, when server returned empty error message
            listOf(UNKNOWN_ERROR)
        }

        return upload.data?.let {
            UploadResult.Success(
                uploadId = it.uploadId
            )
        }?: setError(error, sourceId, file)
    }

    fun setProgressUploader(progress: ProgressCallback?) {
        imageUploaderUseCase.progressCallback = progress
    }

}