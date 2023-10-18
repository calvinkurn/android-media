package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.common.data.consts.POLICY_NOT_FOUND
import com.tokopedia.mediauploader.common.data.consts.TRANSCODING_FAILED
import com.tokopedia.mediauploader.common.data.consts.UPLOAD_ABORT
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.cache.LargeUploadStateCacheManager
import com.tokopedia.mediauploader.common.cache.SourcePolicyManager
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.common.logger.DebugLog
import com.tokopedia.mediauploader.common.logger.onShowDebugLogcat
import com.tokopedia.mediauploader.common.logger.trackToTimber
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isLessThanHoursOf
import com.tokopedia.mediauploader.common.util.slice
import com.tokopedia.mediauploader.common.util.trimLastZero
import com.tokopedia.mediauploader.video.data.params.ChunkCheckerParam
import com.tokopedia.mediauploader.video.data.params.ChunkUploadParam
import com.tokopedia.mediauploader.video.data.params.InitParam
import com.tokopedia.mediauploader.video.data.params.LargeUploadCacheParam
import com.tokopedia.mediauploader.video.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.math.ceil

class LargeUploaderManager @Inject constructor(
    @UploaderQualifier private val policyManager: SourcePolicyManager,
    private val uploadStateManager: LargeUploadStateCacheManager,
    private val initUseCase: InitVideoUploaderUseCase,
    private val checkerUseCase: GetChunkCheckerUseCase,
    private val uploaderUseCase: GetChunkUploaderUseCase,
    private val completeUseCase: SetCompleteUploaderUseCase,
    private val transcodingUseCase: GetTranscodingStatusUseCase,
    private val abortUseCase: SetAbortUploaderUseCase
) {

    private var maxRetryTranscoding = 0
    private var partUploaded = mutableMapOf<Int, Boolean>()
    private var chunkTotal = 0
    private var mUploadId = ""

    private var progressUploader: ProgressUploader? = null

    suspend operator fun invoke(
        file: File,
        sourceId: String,
        withTranscode: Boolean,
        isRetry: Boolean
    ): UploadResult {
        val policy = policyManager.get()
        val videoPolicy = policy?.videoPolicy ?: return UploadResult.Error(POLICY_NOT_FOUND)

        // 1. init the uploader
        getLastState(sourceId, file.name, isRetry) {
            initUpload(sourceId, file)
        }

        // getting the upload size of chunk in MB for calculate the chunk size and as size of part numbers
        val sizePerChunk = videoPolicy.chunkSizePerFileInBytes()

        // calculate the chunk total based on file size and upload size of chunk
        chunkTotal = ceil(file.length() / sizePerChunk.toDouble()).toInt()

        // 2. upload per chunk in loop until N of part number
        uploadPart(file, sizePerChunk, sourceId, policy)

        // 3. set a complete state to check the transcoding and get the video url from it.
        val videoUrl = completeUpload()

        // 4. this using loop for retrying transcoding checker within 5-sec delayed
        var maxRetry = videoPolicy.timeOutOfTranscode() / videoPolicy.retryInterval()

        // in case the policy return unexpected value
        if (maxRetry <= 0) maxRetry = DEFAULT_MAX_RETRY

        if (withTranscode) {
            while (true) {
                if (maxRetryTranscoding >= maxRetry) {
                    resetUpload()
                    trackToTimber(file, sourceId, "$TRANSCODING_FAILED # $mUploadId")
                    return UploadResult.Error(TRANSCODING_FAILED)
                }

                val transcoding = transcodingUseCase(mUploadId)
                if (transcoding.isCompleted()) break

                maxRetryTranscoding++
                delay(videoPolicy.retryIntervalInSec())
            }
        }

        // 5. if the transcoding success, return the video url!
        updateProgressValue()
        resetUpload()

        onShowDebugLogcat(
            DebugLog(
                sourceId = sourceId,
                sourceFile = file.path,
                url = videoUrl,
                uploadId = mUploadId,
                sourcePolicy = policy
            )
        )

        return if (videoUrl.isNotEmpty()) {
            UploadResult.Success(
                videoUrl = videoUrl,
                uploadId = mUploadId
            )
        } else {
            trackToTimber(file, sourceId, "$UPLOAD_ABORT # $mUploadId")
            UploadResult.Error(UPLOAD_ABORT)
        }
    }

    private suspend fun uploadPart(
        file: File,
        sizePerChunk: Int,
        sourceId: String,
        policy: SourcePolicy
    ) = withContext(Dispatchers.IO) {
        val jobList = mutableListOf<Job>()
        for (part in UPLOAD_PART_START..chunkTotal) {
            if (partUploaded[part] == true) continue

            file.slice(part, sizePerChunk)?.let {
                val byteArrayToSend = it

                // trim zero byte from last for the last of part
                if (part == chunkTotal) {
                    byteArrayToSend.trimLastZero()
                }

                jobList.add(launch {
                    chunkUpload(
                        sourceId,
                        file.name,
                        byteArrayToSend,
                        policy.timeOut,
                        partNumber = part
                    )

                    updateProgressValue()
                })
            }
        }
        jobList.forEach { job ->
            job.join()
        }
    }

    suspend fun abortUpload(sourceId: String, fileName: String, abort: suspend () -> Unit) {
        val data = uploadStateManager.get(sourceId, fileName) ?: return

        if (data.uploadId.isEmpty()) {
            error("Seems your session is expired, you cannot abort this upload.")
        }

        val abortUseCase = abortUseCase(data.uploadId)

        if (abortUseCase.isSuccess()) {
            resetUpload()
            abort()
        }
    }

    fun setProgressCallback(progressUploader: ProgressUploader?) {
        this.progressUploader = progressUploader
    }

    private suspend fun getLastState(
        sourceId: String,
        fileName: String,
        isRetry: Boolean,
        init: suspend () -> Unit
    ) {
        val data = uploadStateManager.get(sourceId, fileName)

        if (isRetry.not()) {
            init()
            return
        }

        if (data == null) {
            init()
            return
        }

        if (data.initTimeInMillis.isLessThanHoursOf(THRESHOLD_REQUEST_MAX_TIME)) {
            mUploadId = data.uploadId
            partUploaded = data.partDone.toMutableMap()
        } else {
            resetUpload()
            init()
        }
    }

    private fun updateProgressValue() {
        progressUploader?.onProgress(
            percentage = MAX_PROGRESS_LOADER * partUploaded.size / chunkTotal,
            type = ProgressType.Upload
        )
    }

    private suspend fun initUpload(sourceId: String, file: File) {
        val fileName = file.name

        val init = initUseCase(
            InitParam(
                sourceId = sourceId,
                fileName = fileName
            )
        )

        if (init.isSuccess()) {
            mUploadId = init.uploadId()

            uploadStateManager.set(
                sourceId, LargeUploadCacheParam(
                    filePath = file.path,
                    uploadId = init.uploadId(),
                    partDone = partUploaded,
                    initTimeInMillis = System.currentTimeMillis()
                )
            )
        } else {
            initUpload(sourceId, file)
        }
    }

    private suspend fun chunkUpload(
        sourceId: String,
        fileName: String,
        byteArray: ByteArray,
        timeOut: Int,
        maxRetryCount: Int = MAX_RETRY_COUNT,
        partNumber: Int
    ): Boolean {
        val uploader = uploaderUseCase(
            ChunkUploadParam(
                sourceId = sourceId,
                uploadId = mUploadId,
                partNumber = partNumber.toString(),
                fileName = fileName,
                byteArray = byteArray,
                timeOut = timeOut.toString()
            )
        )

        return if (uploader.isSuccess()) {
            isChunkCorrect(sourceId, fileName, partNumber)
        } else {
            if (maxRetryCount > 0) {
                chunkUpload(
                    sourceId,
                    fileName,
                    byteArray,
                    timeOut,
                    maxRetryCount - 1,
                    partNumber = partNumber
                )
            } else {
                false
            }
        }
    }

    private suspend fun isChunkCorrect(
        sourceId: String,
        fileName: String,
        partNumber: Int
    ): Boolean {
        val checker = checkerUseCase(
            ChunkCheckerParam(
                uploadId = mUploadId,
                partNumber = partNumber.toString(),
                fileName = fileName
            )
        )

        if (checker.isPartSuccess()) {
            partUploaded[partNumber] = true

            uploadStateManager.setPartNumber(sourceId, fileName, partUploaded)
        }

        return checker.isPartSuccess()
    }

    private suspend fun completeUpload(): String {
        if (partUploaded.size >= chunkTotal) {
            val complete = completeUseCase(mUploadId)
            if (complete.isSuccess()) {
                return complete.videoUrl()
            }
        }

        return ""
    }

    private fun resetUpload() {
        uploadStateManager.clear()

        chunkTotal = 0
        partUploaded = mutableMapOf()
        maxRetryTranscoding = 0
    }

    companion object {
        private const val DEFAULT_MAX_RETRY = 24
        private const val MAX_PROGRESS_LOADER = 100

        private const val MAX_RETRY_COUNT = 5
        private const val UPLOAD_PART_START = 1
        private const val THRESHOLD_REQUEST_MAX_TIME = 2 // hours
    }

}
