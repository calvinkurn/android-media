package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.data.VideoUploadServices
import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.data.params.VideoUploaderParam
import com.tokopedia.mediauploader.data.state.ProgressCallback
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetSingleVideoUploaderUseCase @Inject constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<VideoUploaderParam, MediaUploader>(Dispatchers.IO) {

    var progressCallback: ProgressCallback? = null

    override suspend fun execute(params: VideoUploaderParam): MediaUploader {
        if (params.hasNotParams()) throw RuntimeException("No param found")

        val multiPartBodyBuilder = params.videoBody(progressCallback)

        return services.uploadSingleVideo(
            urlToUpload = params.uploadUrl,
            timeOut = params.timeOut,
            body = multiPartBodyBuilder
        )
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

}