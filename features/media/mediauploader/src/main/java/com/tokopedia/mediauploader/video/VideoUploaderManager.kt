package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.common.data.consts.*
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.*
import com.tokopedia.mediauploader.image.data.mapper.ImagePolicyMapper
import com.tokopedia.mediauploader.video.data.params.ChunkCheckerParam
import com.tokopedia.mediauploader.video.data.params.ChunkUploadParam
import com.tokopedia.mediauploader.video.data.params.InitParam
import com.tokopedia.mediauploader.video.data.params.SimpleUploadParam
import com.tokopedia.mediauploader.video.domain.*
import java.io.File

class VideoUploaderManager constructor(
    private val policy: GetVideoPolicyUseCase,
    private val simple: GetSimpleUploaderUseCase,
    private val init: InitVideoUploaderUseCase,
    private val checker: GetChunkCheckerUseCase,
    private val uploader: GetChunkUploaderUseCase,
    private val complete: SetCompleteUploaderUseCase,
    private val abort: SetAbortUploaderUseCase
) : UploaderManager {

    private var hasInit = false

    private var currentUploadId = ""
    private var partNumber = 1

    private suspend fun simpleUpload(file: File, sourceId: String): UploadResult {
        val uploader = simple(SimpleUploadParam(
            sourceId = sourceId,
            file = file,
            timeOut = "120" // mock
        ))

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

    // NEED CHUNK SIZE
    private suspend fun largeUpload(file: File, sourceId: String): UploadResult {
        // init
        if (!hasInit) {
            val init = init(InitParam(
                sourceId = sourceId,
                fileName = file.name
            ))

            currentUploadId = init.uploadId()
            hasInit = true
        }

        // check current chunk
        val checker = checker(ChunkCheckerParam(
            uploadId = currentUploadId,
            partNumber = partNumber.toString(),
            fileName = file.name
        ))

        if (!checker.isPartSuccess()) {
            val uploader = uploader(ChunkUploadParam(
                sourceId = sourceId,
                uploadId = currentUploadId,
                partNumber = partNumber.toString(),
                file = file,
                timeOut = "123"
            ))

            if (uploader.isSuccess()) {
                partNumber++
            } else {
                if (partNumber > 1) {
                    partNumber--
                }
            }
        } else {
            // compare part number and chunk size,
            // call complete here
        }

        return UploadResult.Success("")
    }

    suspend operator fun invoke(
        file: File,
        sourceId: String,
        progress: ProgressCallback?
    ): UploadResult {
        return validate(file, sourceId) { policy ->
            upload(file, sourceId, policy)
        }.also { result ->
            if (result is UploadResult.Error) {
                setError(listOf(result.message), sourceId, file)
            }
        }
    }

    suspend fun upload(
        file: File,
        sourceId: String,
        policy: SourcePolicy
    ): UploadResult {
        return if (file.isMaxFileSize(THRESHOLD_SIMPLE_UPLOAD)) {
            // large upload
            // slice file here
            val fileToUpload = file.slice()

            largeUpload(file, sourceId)
        } else {
            // simple upload
            simpleUpload(file, sourceId)
        }
    }

    suspend fun validate(
        file: File,
        sourceId: String,
        onUpload: suspend (sourcePolicy: SourcePolicy) -> UploadResult
    ): UploadResult {
        // sourceId empty validation
        if (sourceId.isEmpty()) return UploadResult.Error(SOURCE_NOT_FOUND)

        // file full path
        val filePath = file.path

        // request policy by sourceId
        val policyData = policy(sourceId)

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
            onUpload(sourcePolicy)
        }
    }

    companion object {
        private const val THRESHOLD_SIMPLE_UPLOAD = 20 * 1024 * 1024
        private const val SIZE_PER_CHUNK = 10 // in MB
    }

}