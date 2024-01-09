package com.tokopedia.mediauploader.video

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.BaseParam
import com.tokopedia.mediauploader.VideoParam
import com.tokopedia.mediauploader.analytics.UploaderLogger
import com.tokopedia.mediauploader.common.cache.SourcePolicyManager
import com.tokopedia.mediauploader.common.data.consts.TRANSCODING_FAILED
import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.video.data.params.SimpleUploadParam
import com.tokopedia.mediauploader.video.domain.GetSimpleUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetTranscodingStatusUseCase
import kotlinx.coroutines.delay
import javax.inject.Inject

class SimpleUploaderManager @Inject constructor(
    @UploaderQualifier val sourcePolicyManager: SourcePolicyManager,
    private val simpleUploaderUseCase: GetSimpleUploaderUseCase,
    private val transcodingUseCase: GetTranscodingStatusUseCase,
) {

    // set max retry of transcoding checker
    private var maxRetryTranscoding = 0

    suspend operator fun invoke(param: VideoParam): UploadResult {
        val policy = sourcePolicyManager.get() ?: return UploadResult.Error(UNKNOWN_ERROR)
        val base = param.base as BaseParam

        val uploader = simpleUploaderUseCase(
            SimpleUploadParam(
                timeOut = policy.timeOut.orZero().toString(),
                sourceId = base.sourceId,
                file = base.file
            )
        )

        val requestId = uploader.requestId ?: ""
        val error = uploader.errorMessage()
        
        val uploadId = uploader.uploadId.orEmpty()
        val videoUrlResult = uploader.videoUrl.orEmpty()

        if (param.withTranscode && uploadId.isEmpty().not()) {
            while (true) {
                if (maxRetryTranscoding >= MAX_RETRY_TRANSCODING) {
                    return UploadResult.Error(TRANSCODING_FAILED).also {
                        resetUpload()
                    }
                }

                if (uploader.uploadId != null) {
                    val transcode = transcodingUseCase(uploader.uploadId)

                    // transcoding succeed
                    if (transcode.isCompleted()) break

                    // transcoding failed
                    if (transcode.requestId().isNotEmpty()) {
                        return UploadResult.Error(TRANSCODING_FAILED, transcode.requestId())
                    }
                }

                maxRetryTranscoding++
                delay(DELAYED_TO_RETRY)
            }
        }

        return if (videoUrlResult.isEmpty().not()) {
            UploadResult.Success(
                videoUrl = uploader.videoUrl ?: "",
                uploadId = uploader.uploadId.toString()
            )
        } else {
            UploadResult.Error(
                message = error,
                requestId = requestId
            ).also {
                UploaderLogger.commonError(base, it)
            }
        }
    }

    fun setProgressCallback(progressUploader: ProgressUploader?) {
        simpleUploaderUseCase.progressUploader = progressUploader
    }

    private fun resetUpload() {
        maxRetryTranscoding = 0
    }

    companion object {
        private const val MAX_RETRY_TRANSCODING = 24
        private const val DELAYED_TO_RETRY = 5_000L // 5sec
    }
}
