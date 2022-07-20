package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.common.data.consts.TRANSCODING_FAILED
import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR
import com.tokopedia.mediauploader.common.internal.SourcePolicyManager
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.video.data.params.SimpleUploadParam
import com.tokopedia.mediauploader.video.domain.GetSimpleUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetTranscodingStatusUseCase
import kotlinx.coroutines.delay
import java.io.File
import javax.inject.Inject

class SimpleUploaderManager @Inject constructor(
    private val policyManager: SourcePolicyManager,
    private val simpleUploaderUseCase: GetSimpleUploaderUseCase,
    private val transcodingUseCase: GetTranscodingStatusUseCase,
) {

    // set max retry of transcoding checker
    private var maxRetryTranscoding = 0

    suspend operator fun invoke(file: File, sourceId: String, withTranscode: Boolean): UploadResult {
        val uploader = simpleUploaderUseCase(SimpleUploadParam(
            timeOut = policyManager.policy().timeOutString(),
            sourceId = sourceId,
            file = file
        ))

        val error = uploader.errorMessage ?: UNKNOWN_ERROR

        if (withTranscode) {
            while(true) {
                if (maxRetryTranscoding >= MAX_RETRY_TRANSCODING) {
                    resetUpload()

                    return UploadResult.Error(TRANSCODING_FAILED)
                }

                if (uploader.uploadId != null) {
                    val transcode = transcodingUseCase(uploader.uploadId)

                    if (transcode.isCompleted()) {
                        break
                    }
                }

                maxRetryTranscoding++
                delay(DELAYED_TO_RETRY)
            }
        }

        // clear the current policy
        policyManager.clear()

        return uploader.videoUrl?.let {
            UploadResult.Success(
                videoUrl = it,
                uploadId = uploader.uploadId.toString()
            )
        }?: UploadResult.Error(error)
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