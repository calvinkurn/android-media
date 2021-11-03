package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.common.data.consts.CHUNK_UPLOAD
import com.tokopedia.mediauploader.common.data.consts.TRANSCODING_FAILED
import com.tokopedia.mediauploader.common.data.consts.UPLOAD_ABORT
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.mbToBytes
import com.tokopedia.mediauploader.common.util.slice
import com.tokopedia.mediauploader.common.util.trimLastZero
import com.tokopedia.mediauploader.video.data.params.ChunkCheckerParam
import com.tokopedia.mediauploader.video.data.params.ChunkUploadParam
import com.tokopedia.mediauploader.video.data.params.InitParam
import com.tokopedia.mediauploader.video.domain.*
import kotlinx.coroutines.delay
import java.io.File
import javax.inject.Inject
import kotlin.math.ceil

class LargeUploaderManager @Inject constructor(
    private val initUseCase: InitVideoUploaderUseCase,
    private val checkerUseCase: GetChunkCheckerUseCase,
    private val uploaderUseCase: GetChunkUploaderUseCase,
    private val completeUseCase: SetCompleteUploaderUseCase,
    private val transcodingUseCase: GetTranscodingStatusUseCase,
    private val abortUseCase: SetAbortUploaderUseCase
) {

    // this flag to save state the initialization
    private var hasInit = false

    // the part number started 1
    private var partNumber = 1

    // save the current source id
    private var currentSourceId = ""

    // save the current upload id comes from init upload
    private var currentUploadId = ""

    // save the estimation of chunk total
    private var chunkTotal = 0

    // set max retry of transcoding checker
    private var maxRetryTranscoding = 0

    suspend operator fun invoke(file: File, sourceId: String, policy: SourcePolicy, withTranscode: Boolean): UploadResult {
        // getting the upload size of chunk in MB for calculate the chunk size and as size of part numbers
        val sizePerChunk = (policy.videoPolicy?.largeChunkSize?: 10).mbToBytes()

        // calculate the chunk total based on file size and upload size of chunk
        this.chunkTotal = ceil(file.length() / sizePerChunk.toDouble()).toInt()

        // set the current source id
        this.currentSourceId = sourceId

        // 1. init the uploader
        initUpload(sourceId, file.name)

        // 2. upload per chunk in loop until N of part number
        for (part in 1..chunkTotal) {
            if (part < partNumber) continue

            file.slice(part, sizePerChunk)?.let {
                val byteArrayToSend = it

                // trim zero byte from last for the last of part
                if (part == chunkTotal) {
                    byteArrayToSend.trimLastZero()
                }

                // upload it!
                val upload = chunkUpload(file.name, byteArrayToSend, policy.timeOut)

                if (!upload) {
                    // return error if failed but it is continuable
                    return UploadResult.Error(CHUNK_UPLOAD)
                }
            }
        }

        // 3. set a complete state to check the transcoding
        val videoUrl = completeUpload()

        // 4. this using loop for retrying transcoding checker within 5-sec delayed
        if (withTranscode) {
            while(true) {
                if (maxRetryTranscoding >= MAX_RETRY_TRANSCODING) {
                    resetUpload()

                    return UploadResult.Error(TRANSCODING_FAILED)
                }

                if (transcodingUseCase(currentUploadId).isCompleted()) {
                    break
                }

                maxRetryTranscoding++
                delay(5000)
            }
        }

        // 5. if the transcoding success, return the video url!
        return if (videoUrl.isNotEmpty()) {
            resetUpload()

            UploadResult.Success(
                videoUrl = videoUrl,
                uploadId = currentUploadId
            )
        } else {
            UploadResult.Error(UPLOAD_ABORT)
        }
    }

    suspend fun abortUpload(abort: () -> Unit) {
        val abortUseCase = abortUseCase(currentUploadId)

        if (abortUseCase.isSuccess()) {
            resetUpload()
            abort()
        }
    }

    private suspend fun initUpload(sourceId: String, fileName: String) {
        if (hasInit) return

        val init = initUseCase(InitParam(
            sourceId = sourceId,
            fileName = fileName
        ))

        if (init.isSuccess()) {
            currentUploadId = init.uploadId()
            hasInit = true
        } else {
            initUpload(sourceId, fileName)
        }
    }

    private suspend fun chunkUpload(
        fileName: String,
        byteArray: ByteArray,
        timeOut: Int,
        maxRetryCount: Int = 3
    ): Boolean {
        val uploader = uploaderUseCase(ChunkUploadParam(
            sourceId = currentSourceId,
            uploadId = currentUploadId,
            partNumber = partNumber.toString(),
            fileName = fileName,
            byteArray = byteArray,
            timeOut = timeOut.toString()
        ))

        return if (uploader.isSuccess()) {
            isChunkCorrect(fileName)
        } else {
            if (maxRetryCount > 0) {
                chunkUpload(fileName, byteArray, maxRetryCount - 1)
            } else {
                false
            }
        }
    }

    private suspend fun isChunkCorrect(fileName: String): Boolean {
        val checker = checkerUseCase(ChunkCheckerParam(
            uploadId = currentUploadId,
            partNumber = partNumber.toString(),
            fileName = fileName
        ))

        if (checker.isPartSuccess()) {
            partNumber++
        }

        return checker.isPartSuccess()
    }

    private suspend fun completeUpload(): String {
        if (partNumber >= chunkTotal) {
            val complete = completeUseCase(currentUploadId)

            if (complete.isSuccess()) {
                return complete.videoUrl()
            }
        }

        return ""
    }

    private fun resetUpload() {
        maxRetryTranscoding = 0
        currentUploadId = ""
        hasInit = false
        partNumber = 1
    }

    companion object {
        private const val MAX_RETRY_TRANSCODING = 24
    }

}