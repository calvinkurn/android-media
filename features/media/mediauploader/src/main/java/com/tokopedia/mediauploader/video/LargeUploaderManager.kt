package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.common.data.consts.CHUNK_UPLOAD
import com.tokopedia.mediauploader.common.data.consts.POLICY_NOT_FOUND
import com.tokopedia.mediauploader.common.data.consts.TRANSCODING_FAILED
import com.tokopedia.mediauploader.common.data.consts.UPLOAD_ABORT
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isLessThan2Hours
import com.tokopedia.mediauploader.common.util.slice
import com.tokopedia.mediauploader.common.util.trimLastZero
import com.tokopedia.mediauploader.video.data.internal.LargeUploadState
import com.tokopedia.mediauploader.video.data.params.ChunkCheckerParam
import com.tokopedia.mediauploader.video.data.params.ChunkUploadParam
import com.tokopedia.mediauploader.video.data.params.InitParam
import com.tokopedia.mediauploader.video.data.params.LargeUploadCacheParam
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
    private val abortUseCase: SetAbortUploaderUseCase,
    private val uploadState: LargeUploadState,
) {

    private var maxRetryTranscoding = 0
    private var partNumber = 1
    private var chunkTotal = 0
    private var mUploadId = ""

    private var progressUploader: ProgressUploader? = null

    suspend operator fun invoke(file: File, sourceId: String, policy: SourcePolicy, withTranscode: Boolean): UploadResult {
        if (policy.videoPolicy == null) return UploadResult.Error(POLICY_NOT_FOUND)

        // getting the upload size of chunk in MB for calculate the chunk size and as size of part numbers
        val sizePerChunk = policy.videoPolicy.chunkSizePerFileInBytes()

        // calculate the chunk total based on file size and upload size of chunk
        chunkTotal = ceil(file.length() / sizePerChunk.toDouble()).toInt()

        // 1. init the uploader
        getLastState(sourceId, file.name) {
            initUpload(sourceId, file)
        }

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
                val upload = chunkUpload(
                    sourceId,
                    file.name,
                    byteArrayToSend,
                    policy.timeOut
                )

                if (!upload) {
                    // return error if failed but it is continuable
                    return UploadResult.Error(CHUNK_UPLOAD)
                }
            }

            updateProgressValue(part - 1)
        }

        // 3. set a complete state to check the transcoding and get the video url from it.
        val videoUrl = completeUpload()

        // 4. this using loop for retrying transcoding checker within 5-sec delayed
        if (withTranscode) {
            while(true) {
                if (maxRetryTranscoding >= MAX_RETRY_TRANSCODING) {
                    resetUpload()
                    return UploadResult.Error(TRANSCODING_FAILED)
                }

                val transcoding = transcodingUseCase(mUploadId)
                if (transcoding.isCompleted()) break

                maxRetryTranscoding++
                delay(policy.videoPolicy.retryIntervalInSec())
            }
        }

        // 5. if the transcoding success, return the video url!
        updateProgressValue(chunkTotal)
        resetUpload()

        return if (videoUrl.isNotEmpty()) {
            UploadResult.Success(
                videoUrl = videoUrl,
                uploadId = mUploadId
            )
        } else {
            UploadResult.Error(UPLOAD_ABORT)
        }
    }

    suspend fun abortUpload(abort: () -> Unit) {
        val abortUseCase = abortUseCase(mUploadId)

        if (abortUseCase.isSuccess()) {
            resetUpload()
            abort()
        }
    }

    fun setProgressCallback(progressUploader: ProgressUploader?) {
        this.progressUploader = progressUploader
    }

    private suspend fun getLastState(sourceId: String, fileName: String, init: suspend () -> Unit) {
        val data = uploadState.get(sourceId, fileName)

        if (data != null) {
            if (data.initTimeInMillis.isLessThan2Hours()) {
                mUploadId = data.uploadId
                partNumber = data.partNumber
            } else {
                init()
            }
        } else {
            init()
        }
    }

    private fun updateProgressValue(value: Int) {
        progressUploader?.onProgress(MAX_PROGRESS_LOADER * value / chunkTotal)
    }

    private suspend fun initUpload(sourceId: String, file: File) {
        val fileName = file.name

        val init = initUseCase(InitParam(
            sourceId = sourceId,
            fileName = fileName
        ))

        if (init.isSuccess()) {
            mUploadId = init.uploadId()

            uploadState.set(sourceId, LargeUploadCacheParam(
                filePath = file.path,
                uploadId = init.uploadId(),
                // initialize upload, the part starts from 1
                partNumber = 1,
                initTimeInMillis = System.currentTimeMillis()
            ))
        } else {
            initUpload(sourceId, file)
        }
    }

    private suspend fun chunkUpload(
        sourceId: String,
        fileName: String,
        byteArray: ByteArray,
        timeOut: Int,
        maxRetryCount: Int = 5
    ): Boolean {
        val uploader = uploaderUseCase(ChunkUploadParam(
            sourceId = sourceId,
            uploadId = mUploadId,
            partNumber = partNumber.toString(),
            fileName = fileName,
            byteArray = byteArray,
            timeOut = timeOut.toString()
        ))

        return if (uploader.isSuccess()) {
            isChunkCorrect(sourceId, fileName)
        } else {
            if (maxRetryCount > 0) {
                chunkUpload(sourceId, fileName, byteArray, maxRetryCount - 1)
            } else {
                false
            }
        }
    }

    private suspend fun isChunkCorrect(sourceId: String, fileName: String): Boolean {
        val checker = checkerUseCase(ChunkCheckerParam(
            uploadId = mUploadId,
            partNumber = partNumber.toString(),
            fileName = fileName
        ))

        if (checker.isPartSuccess()) {
            partNumber++

            uploadState.setPartNumber(sourceId, fileName, partNumber)
        }

        return checker.isPartSuccess()
    }

    private suspend fun completeUpload(): String {
        if (partNumber >= chunkTotal) {
            val complete = completeUseCase(mUploadId)

            if (complete.isSuccess()) {
                return complete.videoUrl()
            }
        }

        return ""
    }

    private fun resetUpload() {
        uploadState.clear()

        chunkTotal = 0
        partNumber = 1
        mUploadId = ""
        maxRetryTranscoding = 0
    }

    companion object {
        private const val MAX_RETRY_TRANSCODING = 24
        private const val MAX_PROGRESS_LOADER = 100
    }

}