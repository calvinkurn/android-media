package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.video.data.params.SimpleUploadParam
import com.tokopedia.mediauploader.video.domain.GetSimpleUploaderUseCase
import java.io.File
import javax.inject.Inject

class SimpleUploaderManager @Inject constructor(val simpleUploaderUseCase: GetSimpleUploaderUseCase) {

    suspend operator fun invoke(file: File, sourceId: String, policy: SourcePolicy): UploadResult {
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

        return if (!uploader.videoUrl.isNullOrBlank()) {
            UploadResult.Success(videoUrl = uploader.videoUrl)
        } else {
            UploadResult.Error(error)
        }
    }

}