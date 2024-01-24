package com.tokopedia.mediauploader.video

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mediauploader.BaseParam
import com.tokopedia.mediauploader.VideoParam
import com.tokopedia.mediauploader.analytics.UploaderLogger
import com.tokopedia.mediauploader.common.cache.LargeUploadStateCacheManager
import com.tokopedia.mediauploader.common.cache.SourcePolicyManager
import com.tokopedia.mediauploader.common.data.consts.CHUNK_UPLOAD
import com.tokopedia.mediauploader.common.data.consts.POLICY_NOT_FOUND
import com.tokopedia.mediauploader.common.data.consts.TRANSCODING_FAILED
import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR
import com.tokopedia.mediauploader.common.data.consts.UPLOAD_ABORT
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isLessThanHoursOf
import com.tokopedia.mediauploader.common.util.slice
import com.tokopedia.mediauploader.common.util.clearFileSliceStorage
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
import kotlin.math.min

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
    private var requestId = ""

    // used by recursive upload as flag, set as 1 since upload first is outside the recursive flow
    private var partUploadProgress = 1
    private var threadLimit: Int = 0

    suspend operator fun invoke(param: VideoParam): UploadResult {
        threadLimit = 0
        partUploadProgress = 1

        val base = param.base as BaseParam

        val policy = policyManager.get()
        val videoPolicy = policy?.videoPolicy ?: return UploadResult.Error(POLICY_NOT_FOUND)

        // 1. init the uploader
        if (shouldAbleToInitUpload(param).not()) {
            val init = initUpload(base)

            if (!init.isSuccess()) {
                return UploadResult.Error(CHUNK_UPLOAD, init.requestId.toString())
            }
        }

        // getting the upload size of chunk in MB for calculate the chunk size and as size of part numbers
        val sizePerChunk = videoPolicy.chunkSizePerFileInBytes()

        // calculate the chunk total based on file size and upload size of chunk
        chunkTotal = ceil(base.file.length() / sizePerChunk.toDouble()).toInt()

        // 2. upload per chunk in loop until N of part number
        val isFirstPartUploadSucceed = uploadFirstPart(base.file, sizePerChunk, base.sourceId, policy)

        if (isFirstPartUploadSucceed) {
            // 3. Upload the remaining parts (2 ~ [chunkTotal])
            uploadPart(base.file, sizePerChunk, base.sourceId, policy)
        } else {
            UploaderLogger.commonError(base, CHUNK_UPLOAD, requestId)
            return UploadResult.Error(CHUNK_UPLOAD, requestId)
        }

        // 4. if the transcoding success, return the video url!
        val result = completeUpload()

        // 5. set a complete state to check the transcoding and get the video url from it.
        if (param.withTranscode) {
            val transcode = waitTranscode(videoPolicy)

            if (transcode != null) {
                return transcode.also {
                    UploaderLogger.commonWithoutReqIdError(
                        param = base,
                        message = "Fail to transcode (uploadId=$mUploadId; file=${base.file.name})"
                    )
                }
            }
        }

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
                UploaderLogger.commonError(base, it)
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
                // transcode timeout
                return getTranscodeError()
            }

            val transcodeStatus = transcodingUseCase(mUploadId)

            // transcode success
            if (transcodeStatus.isCompleted()) break

            // transcode failed
            if (transcodeStatus.requestId().isNotEmpty() || transcodeStatus.isFailed()) {
                return getTranscodeError(transcodeStatus.requestId())
            }

            maxRetryTranscoding++
            delay(policy.retryIntervalInSec())
        }

        return null
    }

    private fun getTranscodeError(requestId: String? = null): UploadResult.Error {
        return UploadResult.Error(TRANSCODING_FAILED, requestId ?: TRANSCODE_FAILED_CODE).also { resetUpload() }
    }

    private suspend fun uploadFirstPart(
        file: File,
        sizePerChunk: Int,
        sourceId: String,
        policy: SourcePolicy
    ): Boolean {
        val maxUploadChunk = policy.videoPolicy?.largeMaxConcurrent ?: 1
        threadLimit = min(maxUploadChunk, chunkTotal)

        if (partUploaded[UPLOAD_FIRST_INDEX] == true) return true

        file.slice(UPLOAD_FIRST_INDEX, sizePerChunk, reuseSlot = null, slotSize = threadLimit)?.let { (_, byteArrayToSend) ->
            return byteArrayToSend?.let {
                return chunkUpload(
                    sourceId,
                    file,
                    it,
                    policy.timeOut,
                    UPLOAD_FIRST_INDEX
                ).also {
                    updateProgressValue()
                }
            } ?: false
        } ?: return false
    }

    private suspend fun uploadPart(
        file: File,
        sizePerChunk: Int,
        sourceId: String,
        policy: SourcePolicy
    ) = withContext(dispatchers.io) {
        val jobList = mutableListOf<Job>()

        for (part in UPLOAD_PART_INDEX..chunkTotal) {
            if (partUploaded[part] == true) continue
            if (requestId.isNotEmpty()) error(UNKNOWN_ERROR)

            // stop job creation if job number is already on thread limit
            if (jobList.size >= threadLimit) break

            jobList.add(launch {
                recursivePartUpload(
                    file, sizePerChunk, sourceId, policy
                )
            })
        }

        jobList.forEach {
            it.join()
        }
    }

    private suspend fun recursivePartUpload(
        file: File,
        sizePerChunk: Int,
        sourceId: String,
        policy: SourcePolicy,
        reuseSlot: Int? = null
    ) {
        partUploadProgress++
        val part = partUploadProgress

        withContext(dispatchers.io) {
            if (partUploaded[part] == true) return@withContext
            if (partUploadProgress > chunkTotal) {
                clearFileSliceStorage()
                return@withContext
            }

            var job: Job? = null

            file.slice(part, sizePerChunk, reuseSlot = reuseSlot, slotSize = threadLimit)?.let { (slotIndex, byteArrayToSend) ->
                byteArrayToSend?.let {
                    // trim zero byte from last for the last of part
                    if (part == chunkTotal) {
                        byteArrayToSend.trimLastZero()
                    }

                    job = launch {
                        chunkUpload(
                            sourceId,
                            file,
                            byteArrayToSend,
                            policy.timeOut,
                            partNumber = part
                        )

                        updateProgressValue()
                        recursivePartUpload(
                            file, sizePerChunk, sourceId, policy, reuseSlot = slotIndex
                        )
                    }
                }
            }

            job?.join()
        }
    }

    suspend fun abortUpload(sourceId: String, file: File, abort: suspend () -> Unit) {
        val data = uploadStateManager.get(sourceId, file.name) ?: return
        if (data.uploadId.isEmpty()) error("Seems your session is expired, you cannot abort this upload.")

        val abortUseCase = abortUseCase(data.uploadId)
        if (abortUseCase.isSuccess()) {
            resetUpload()
            abort()
        } else {
            UploaderLogger.commonError(
                param = BaseParam.create(file, sourceId),
                message = "Fail to abort upload (uploadId=${data.uploadId}; file=${file.name})",
                reqId = abortUseCase.requestId.toString()
            )
        }
    }

    fun setProgressCallback(progressUploader: ProgressUploader?) {
        this.progressUploader = progressUploader
    }

    private fun shouldAbleToInitUpload(param: VideoParam): Boolean {
        val base = param.base as BaseParam
        val data = uploadStateManager.get(base.sourceId, base.file.name)

        // these both validation indicates that the uploader needs to init the uploader.
        if (param.ableToRetry.not()) return false
        if (data == null) return false

        return if (data.initTimeInMillis.isLessThanHoursOf(THRESHOLD_REQUEST_MAX_TIME)) {
            mUploadId = data.uploadId
            partUploaded = data.partDone.toMutableMap()

            // the uploader doesn't need to re-init
            true
        } else {

            // forcibly init the uploader
            resetUpload()
            false
        }
    }

    private fun updateProgressValue() {
        progressUploader?.onProgress(
            percentage = MAX_PROGRESS_LOADER * partUploaded.size / chunkTotal,
            type = ProgressType.Upload
        )
    }

    private suspend fun initUpload(param: BaseParam): LargeUploader {
        val fileName = param.file.name

        return initUseCase(
            InitParam(
                sourceId = param.sourceId,
                fileName = fileName
            )
        ).also {
            if (it.isSuccess()) {
                mUploadId = it.uploadId()

                uploadStateManager.set(
                    param.sourceId, LargeUploadCacheParam(
                        filePath = param.file.path,
                        uploadId = it.uploadId(),
                        partDone = partUploaded,
                        initTimeInMillis = System.currentTimeMillis()
                    )
                )
            } else {
                UploaderLogger.commonError(
                    param = param,
                    message = "Fail to init (file=$fileName)",
                    reqId = it.requestId.toString()
                )
            }
        }
    }

    private suspend fun chunkUpload(
        sourceId: String,
        file: File,
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
                fileName = file.name,
                byteArray = byteArray,
                timeOut = timeOut.toString()
            )
        )

        return if (uploader.isSuccess()) {
            isChunkCorrect(sourceId, file, partNumber)
        } else {
            if (requestId.isEmpty()) {
                requestId = uploader.requestId.toString()
            }

            UploaderLogger.commonError(
                param = BaseParam.create(file, sourceId),
                message = "Fail to upload (uploadId=$mUploadId; file=${file.name}; part=$partNumber)",
                reqId = uploader.requestId.toString()
            )

            if (maxRetryCount > 0) {
                chunkUpload(
                    sourceId,
                    file,
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
        file: File,
        partNumber: Int
    ): Boolean {
        val checker = checkerUseCase(
            ChunkCheckerParam(
                uploadId = mUploadId,
                partNumber = partNumber.toString(),
                fileName = file.name
            )
        )

        val isChuckSucceed = checker.isPartSuccess()

        if (isChuckSucceed) {
            partUploaded[partNumber] = true
            uploadStateManager.setPartNumber(sourceId, file.name, partUploaded)
        } else {
            UploaderLogger.commonError(
                param = BaseParam.create(file, sourceId),
                message = "Fail to check chunk (uploadId=$mUploadId; file=${file.name}; part=$partNumber)",
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
        requestId = ""
        chunkTotal = 0
        maxRetryTranscoding = 0
        uploadStateManager.clear()
        partUploaded = mutableMapOf()
    }

    companion object {
        private const val DEFAULT_MAX_RETRY = 24
        private const val MAX_PROGRESS_LOADER = 100

        private const val MAX_RETRY_COUNT = 5
        private const val THRESHOLD_REQUEST_MAX_TIME = 2 // hours

        private const val UPLOAD_FIRST_INDEX = 1
        private const val UPLOAD_PART_INDEX = 2

        private const val TRANSCODE_FAILED_CODE = "-2"
    }
}
