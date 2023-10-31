package com.tokopedia.mediauploader.video

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mediauploader.BaseParam
import com.tokopedia.mediauploader.VideoParam
import com.tokopedia.mediauploader.analytics.UploaderLogger
import com.tokopedia.mediauploader.common.cache.LargeUploadStateCacheManager
import com.tokopedia.mediauploader.common.cache.SourcePolicyManager
import com.tokopedia.mediauploader.common.data.consts.POLICY_NOT_FOUND
import com.tokopedia.mediauploader.common.data.consts.TRANSCODING_FAILED
import com.tokopedia.mediauploader.common.data.consts.UPLOAD_ABORT
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isLessThanHoursOf
import com.tokopedia.mediauploader.common.util.slice
import com.tokopedia.mediauploader.common.util.trimLastZero
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import com.tokopedia.mediauploader.video.data.entity.VideoPolicy
import com.tokopedia.mediauploader.video.data.params.ChunkCheckerParam
import com.tokopedia.mediauploader.video.data.params.ChunkUploadParam
import com.tokopedia.mediauploader.video.data.params.InitParam
import com.tokopedia.mediauploader.video.data.params.LargeUploadCacheParam
import com.tokopedia.mediauploader.video.domain.GetChunkCheckerUseCase
import com.tokopedia.mediauploader.video.domain.GetChunkUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetTranscodingStatusUseCase
import com.tokopedia.mediauploader.video.domain.InitVideoUploaderUseCase
import com.tokopedia.mediauploader.video.domain.SetAbortUploaderUseCase
import com.tokopedia.mediauploader.video.domain.SetCompleteUploaderUseCase
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
    private val abortUseCase: SetAbortUploaderUseCase,
    private val dispatchers: CoroutineDispatchers
) {

    private var maxRetryTranscoding = 0
    private var partUploaded = mutableMapOf<Int, Boolean>()
    private var chunkTotal = 0
    private var mUploadId = ""

    private var progressUploader: ProgressUploader? = null

    suspend operator fun invoke(param: VideoParam): UploadResult {
        val base = param.base as BaseParam

        val policy = policyManager.get()
        val videoPolicy = policy?.videoPolicy ?: return UploadResult.Error(POLICY_NOT_FOUND)

        // 1. init the uploader
        getLastState(param, ::initUpload)

        // getting the upload size of chunk in MB for calculate the chunk size and as size of part numbers
        val sizePerChunk = videoPolicy.chunkSizePerFileInBytes()

        // calculate the chunk total based on file size and upload size of chunk
        chunkTotal = ceil(base.file.length() / sizePerChunk.toDouble()).toInt()

        // 2. upload per chunk in loop until N of part number
        uploadPart(base.file, sizePerChunk, base.sourceId, policy)

        // 3. set a complete state to check the transcoding and get the video url from it.
        val result = completeUpload()

        if (param.withTranscode) {
            val transcode = waitTranscode(videoPolicy)

            if (transcode != null) {
                return transcode.also {
                    UploaderLogger.commonWithoutReqIdError(
                        base.sourceId,
                        "$TRANSCODING_FAILED # $mUploadId"
                    )
                }
            }
        }

        // 5. if the transcoding success, return the video url!
        updateProgressValue()
        resetUpload()

        return if (result?.isSuccess() == true) {
            UploadResult.Success(
                videoUrl = result.videoUrl(),
                uploadId = mUploadId
            )
        } else {
            UploadResult.Error(
                message = UPLOAD_ABORT,
                requestId = result?.requestId.toString()
            ).also {
                UploaderLogger.commonError(
                    sourceId = base.sourceId,
                    error = it
                )
            }
        }
    }

    private suspend fun waitTranscode(policy: VideoPolicy): UploadResult? {
        // this using loop for retrying transcoding checker within 5-sec delayed
        var maxRetry = policy.timeOutOfTranscode() / policy.retryInterval()

        // in case the policy return unexpected value
        if (maxRetry <= 0) maxRetry = DEFAULT_MAX_RETRY

        while (true) {
            if (maxRetryTranscoding >= maxRetry) {
                return UploadResult.Error(TRANSCODING_FAILED).also { resetUpload() }
            }

            if (transcodingUseCase(mUploadId).isCompleted()) break

            maxRetryTranscoding++
            delay(policy.retryIntervalInSec())
        }

        return null
    }

    private suspend fun uploadPart(file: File, sizePerChunk: Int, sourceId: String, policy: SourcePolicy) =
        withContext(dispatchers.io) {
            val jobList = mutableListOf<Job>()

            for (part in UPLOAD_PART_START..chunkTotal) {
                if (partUploaded[part] == true) continue

                file.slice(part, sizePerChunk)?.let {
                    val byteArrayToSend = it

                    // trim zero byte from last for the last of part
                    if (part == chunkTotal) byteArrayToSend.trimLastZero()

                    jobList.add(
                        launch {
                            chunkUpload(
                                sourceId,
                                file.name,
                                byteArrayToSend,
                                policy.timeOut,
                                part
                            )

                            updateProgressValue()
                        }
                    )
                }
            }

            jobList.forEach { job ->
                job.join()
            }
        }

    suspend fun abortUpload(sourceId: String, fileName: String, abort: suspend () -> Unit) {
        val data = uploadStateManager.get(sourceId, fileName) ?: return
        if (data.uploadId.isEmpty()) error("Seems your session is expired, you cannot abort this upload.")

        val abortUseCase = abortUseCase(data.uploadId)
        if (abortUseCase.isSuccess()) {
            resetUpload()
            abort()
        } else {
            UploaderLogger.commonError(
                sourceId = sourceId,
                message = "Fail to abort upload",
                reqId = abortUseCase.requestId.toString()
            )
        }
    }

    fun setProgressCallback(progressUploader: ProgressUploader?) {
        this.progressUploader = progressUploader
    }

    private suspend fun getLastState(param: VideoParam, init: suspend (BaseParam) -> Unit) {
        val base = param.base as BaseParam
        val data = uploadStateManager.get(base.sourceId, base.file.name)

        if (param.ableToRetry.not()) {
            init(base)
            return
        }

        if (data == null) {
            init(base)
            return
        }

        if (data.initTimeInMillis.isLessThanHoursOf(THRESHOLD_REQUEST_MAX_TIME)) {
            mUploadId = data.uploadId
            partUploaded = data.partDone.toMutableMap()
        } else {
            resetUpload()
            init(base)
        }
    }

    private fun updateProgressValue() {
        progressUploader?.onProgress(
            percentage = MAX_PROGRESS_LOADER * partUploaded.size / chunkTotal,
            type = ProgressType.Upload
        )
    }

    private suspend fun initUpload(param: BaseParam) {
        val fileName = param.file.name

        val init = initUseCase(
            InitParam(
                sourceId = param.sourceId,
                fileName = fileName
            )
        )

        if (init.isSuccess()) {
            mUploadId = init.uploadId()

            uploadStateManager.set(
                param.sourceId, LargeUploadCacheParam(
                    filePath = param.file.path,
                    uploadId = init.uploadId(),
                    partDone = partUploaded,
                    initTimeInMillis = System.currentTimeMillis()
                )
            )
        } else {
            UploaderLogger.commonError(
                sourceId = param.sourceId,
                message = "Fail to init (file=$fileName)",
                reqId = init.requestId.toString()
            )

            initUpload(param)
        }
    }

    private suspend fun chunkUpload(
        sourceId: String,
        fileName: String,
        byteArray: ByteArray,
        timeOut: Int,
        partNumber: Int,
        maxRetryCount: Int = MAX_RETRY_COUNT,
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
            UploaderLogger.commonError(
                sourceId = sourceId,
                message = "Fail to upload (uploadId=$mUploadId; file=$fileName; part=$partNumber)",
                reqId = uploader.requestId.toString()
            )

            if (maxRetryCount > 0) {
                chunkUpload(
                    sourceId,
                    fileName,
                    byteArray,
                    timeOut,
                    partNumber,
                    maxRetryCount - 1,
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

        val isChuckSucceed = checker.isPartSuccess()

        if (isChuckSucceed) {
            partUploaded[partNumber] = true
            uploadStateManager.setPartNumber(sourceId, fileName, partUploaded)
        } else {
            UploaderLogger.commonError(
                sourceId = sourceId,
                message = "Fail to check chunk (uploadId=$mUploadId; file=$fileName; part=$partNumber)",
                reqId = checker.requestId.toString()
            )
        }

        return isChuckSucceed
    }

    private suspend fun completeUpload(): LargeUploader? {
        if (partUploaded.size >= chunkTotal) {
            return completeUseCase(mUploadId)
        }

        return null
    }

    private fun resetUpload() {
        chunkTotal = 0
        maxRetryTranscoding = 0
        uploadStateManager.clear()
        partUploaded = mutableMapOf()
    }

    companion object {
        private const val DEFAULT_MAX_RETRY = 24
        private const val MAX_PROGRESS_LOADER = 100

        private const val MAX_RETRY_COUNT = 5
        private const val UPLOAD_PART_START = 1
        private const val THRESHOLD_REQUEST_MAX_TIME = 2 // hours
    }
}
