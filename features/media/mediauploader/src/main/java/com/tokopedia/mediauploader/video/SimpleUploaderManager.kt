package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.common.data.consts.TRANSCODING_FAILED
import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.video.data.params.SimpleUploadParam
import com.tokopedia.mediauploader.video.domain.GetSimpleUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetTranscodingStatusUseCase
import kotlinx.coroutines.delay
import java.io.File
import javax.inject.Inject

class SimpleUploaderManager @Inject constructor(
    val simpleUploaderUseCase: GetSimpleUploaderUseCase,
    val transcodingUseCase: GetTranscodingStatusUseCase,
) {

    // set max retry of transcoding checker
    private var maxRetryTranscoding = 0

    suspend operator fun invoke(file: File, sourceId: String, policy: SourcePolicy, withTranscode: Boolean): UploadResult {
        val uploader = simpleUploaderUseCase(SimpleUploadParam(
            timeOut = policy.timeOut.toString(),
            sourceId = sourceId,
            file = file
        ))

        val error = if (!uploader.errorMessage.isNullOrBlank()) {
            uploader.errorMessage
        } else {
            UNKNOWN_ERROR
        }

        if (withTranscode) {
            while(true) {
                if (maxRetryTranscoding >= MAX_RETRY_TRANSCODING) {
                    resetUpload()

                    return UploadResult.Error(TRANSCODING_FAILED)
                }

                if (uploader.uploadId != null) {
                    if (transcodingUseCase(uploader.uploadId).isCompleted()) {
                        break
                    }
                }

                maxRetryTranscoding++
                delay(5000)
            }
        }

        return if (!uploader.videoUrl.isNullOrBlank()) {
            UploadResult.Success(
                videoUrl = uploader.videoUrl,
                uploadId = uploader.uploadId.toString()
            )
        } else {
            UploadResult.Error(error)
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
    }

}