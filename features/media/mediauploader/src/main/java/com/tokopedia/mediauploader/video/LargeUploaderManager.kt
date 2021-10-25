package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.slice
import com.tokopedia.mediauploader.video.data.params.*
import com.tokopedia.mediauploader.video.domain.*
import java.io.File

class LargeUploaderManager constructor(
    private val initUseCase: InitVideoUploaderUseCase,
    private val checkerUseCase: GetChunkCheckerUseCase,
    private val uploaderUseCase: GetChunkUploaderUseCase,
    private val completeUseCase: SetCompleteUploaderUseCase,
    private val abortUseCase: SetAbortUploaderUseCase
) : UploaderManager {

    private var hasInit = false

    private var currentUploadId = ""
    private var partNumber = 1

    suspend operator fun invoke(file: File, sourceId: String, accessToken: String): UploadResult {
        var status: UploadResult? = null
        val files = file.slice()

        files.forEach {
            status = largeUpload(it, sourceId, files.size, accessToken)
        }

        return if (status != null) {
            status as UploadResult
        } else {
            UploadResult.Error("")
        }
    }

    suspend fun abortUpload(accessToken: String, abort: () -> Unit) {
        val abortUseCase = abortUseCase(
            AbortParam(
                currentUploadId,
                accessToken
            )
        )

        if (abortUseCase.isSuccess()) {
            abort()
        }
    }

    private suspend fun largeUpload(
        file: File,
        sourceId: String,
        chunkSize: Int,
        accessToken: String
    ): UploadResult {
        // init
        if (!hasInit) {
            val init = initUseCase(
                InitParam(
                    sourceId = sourceId,
                    fileName = file.name
                )
            )

            currentUploadId = init.uploadId()
            hasInit = true
        }

        // check current chunk
        val checker = checkerUseCase(
            ChunkCheckerParam(
                uploadId = currentUploadId,
                partNumber = partNumber.toString(),
                fileName = file.name
            )
        )

        if (!checker.isPartSuccess()) {
            // upload
            val uploader = uploaderUseCase(
                ChunkUploadParam(
                    sourceId = sourceId,
                    uploadId = currentUploadId,
                    partNumber = partNumber.toString(),
                    file = file,
                    timeOut = "123"
                )
            )

            if (uploader.isSuccess()) {
                partNumber++
            } else {
                if (partNumber > 1) partNumber--

                // failed to upload an existing part, retry it!
                largeUpload(file, sourceId, chunkSize, accessToken)
            }
        } else {
            // complete
            if (partNumber >= chunkSize) {
                val complete = completeUseCase(
                    CompleteParam(
                        uploadId = currentUploadId,
                        fileName = file.name,
                        accessToken = accessToken
                    )
                )

                if (complete.isSuccess()) {
                    // return video url
                    return UploadResult.Success(
                        videoUrl = complete.videoUrl()
                    )
                } else {
                    // retry to complete
                    // TODO
                }
            }
        }

        return UploadResult.Error("")
    }

}