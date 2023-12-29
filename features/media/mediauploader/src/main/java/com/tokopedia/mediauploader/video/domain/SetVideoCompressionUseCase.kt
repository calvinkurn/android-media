package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.video.data.params.VideoCompressionParam
import com.tokopedia.mediauploader.video.data.repository.VideoCompressionRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SetVideoCompressionUseCase @Inject constructor(
    @UploaderQualifier private val videoCompressionRepository: VideoCompressionRepository
) : CoroutineUseCase<VideoCompressionParam, String>(Dispatchers.IO) {

    var progressUploader: ProgressUploader? = null

    override suspend fun execute(params: VideoCompressionParam): String {
        return videoCompressionRepository(progressUploader, params)
    }

    override fun graphqlQuery() = "" // no-op
}
