package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.VideoLargeUploader
import com.tokopedia.mediauploader.video.data.params.VideoUploaderParam
import com.tokopedia.mediauploader.video.data.params.VideoUploaderParam.Companion.urlCompleteLargeUploadUrl
import kotlinx.coroutines.Dispatchers

class GetCompeteVideoUploaderUseCase constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<VideoUploaderParam, VideoLargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: VideoUploaderParam): VideoLargeUploader {
        return services.completeLargeUpload(
            urlToUpload = urlCompleteLargeUploadUrl(),
            fileName = params.file.name,
            uploadId = params.uploadId,
            accessToken = params.accessToken
        )
    }

    override fun graphqlQuery() = ""

}