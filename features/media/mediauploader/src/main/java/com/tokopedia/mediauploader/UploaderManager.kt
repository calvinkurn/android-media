package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.data.consts.*
import com.tokopedia.mediauploader.data.entity.SourcePolicy
import com.tokopedia.mediauploader.data.mapper.ImagePolicyMapper
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.DataPolicyUseCase
import com.tokopedia.mediauploader.domain.MediaUploaderUseCase
import com.tokopedia.mediauploader.util.getFileExtension
import com.tokopedia.mediauploader.util.trackToTimber
import java.io.File

class UploaderManager constructor(
    private val dataPolicyUseCase: DataPolicyUseCase,
    private val mediaUploaderUseCase: MediaUploaderUseCase
) {

    suspend fun post(fileToUpload: File, sourceId: String, policy: SourcePolicy): UploadResult {
        // media uploader
        val uploaderParams = mediaUploaderUseCase.createParams(
            uploadUrl = UrlBuilder.generate(policy.host, sourceId),
            filePath = fileToUpload.path,
            timeOut = policy.timeOut.toString()
        )

        // upload file
        val upload = mediaUploaderUseCase(uploaderParams)

        // getting error from gql response
        val error = if (upload.header.messages.isNotEmpty()) {
            upload.header.messages
        } else {
            // error handling, when server returned empty error message
            listOf(UNKNOWN_ERROR)
        }

        return upload.data?.let {
            UploadResult.Success(it.uploadId)
        }?: setError(error, sourceId, fileToUpload)
    }

    /*
    * pre-validation (local), for:
    * - file not sound
    * - source not sound
    * */
    suspend inline fun validate(
        fileToUpload: File,
        sourceId: String,
        onUpload: (sourcePolicy: SourcePolicy) -> UploadResult
    ): UploadResult {
        // sourceId empty validation
        if (sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        // request policy by sourceId
        val sourcePolicy = mediaPolicy(sourceId)

        // get acceptable extension based on policy
        val extensions = sourcePolicy.imagePolicy.extension.split(",")

        // file full path
        val filePath = fileToUpload.path

        return when {
            !fileToUpload.exists() -> UploadResult.Error(FILE_NOT_FOUND)
            !extensions.contains(getFileExtension(filePath)) -> UploadResult.Error(
                formatNotAllowedMessage(sourcePolicy.imagePolicy.extension)
            )
            else -> onUpload(sourcePolicy)
        }
    }

    fun setError(message: List<String>, sourceId: String, fileToUpload: File): UploadResult {
        trackToTimber(fileToUpload, sourceId, message)
        return UploadResult.Error(message.first())
    }

    suspend fun mediaPolicy(sourceId: String): SourcePolicy {
        val dataPolicyParams = dataPolicyUseCase.createParams(sourceId)
        val policyData = dataPolicyUseCase(dataPolicyParams)
        return ImagePolicyMapper.mapToSourcePolicy(policyData.dataPolicy)
    }

}