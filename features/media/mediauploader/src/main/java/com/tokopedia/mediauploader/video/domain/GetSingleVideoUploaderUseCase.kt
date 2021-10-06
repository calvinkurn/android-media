package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.VideoUploader
import com.tokopedia.mediauploader.video.data.params.VideoUploaderParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetSingleVideoUploaderUseCase @Inject constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<VideoUploaderParam, VideoUploader>(Dispatchers.IO) {

    var progressCallback: ProgressCallback? = null

    override suspend fun execute(params: VideoUploaderParam): VideoUploader {
        if (params.hasNotParams()) throw RuntimeException("No param found")

        val videoFileBody = params.videoBody(progressCallback)
        val videoFileNameBody = params.videoFileName()

        return services.uploadSingleVideo(
            urlToUpload = params.uploadUrl,
            timeOut = params.timeOut,
            videoFile = videoFileBody,
            fileName = videoFileNameBody
        )
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

}