package com.tokopedia.mediauploader.video

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.common.data.consts.TRANSCODING_FAILED
import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR
import com.tokopedia.mediauploader.common.internal.SourcePolicyManager
import com.tokopedia.mediauploader.common.logger.DebugLog
import com.tokopedia.mediauploader.common.logger.onShowDebugLogcat
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
        val policy = policyManager.get()

        val uploader = simpleUploaderUseCase(SimpleUploadParam(
            timeOut = policy?.timeOut.orZero().toString(),
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

        onShowDebugLogcat(
            DebugLog(
                sourceId = sourceId,
                sourceFile = file.path,
                url = uploader.videoUrl.toString(),
                uploadId = uploader.uploadId.toString(),
                sourcePolicy = policy
            )
        )

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