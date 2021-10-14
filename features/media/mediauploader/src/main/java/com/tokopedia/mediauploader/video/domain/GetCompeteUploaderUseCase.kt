package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.consts.UrlBuilder
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.VideoLargeUploader
import com.tokopedia.mediauploader.video.data.params.CompleteParam
import kotlinx.coroutines.Dispatchers

class GetCompeteVideoUploaderUseCase constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<CompleteParam, VideoLargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: CompleteParam): VideoLargeUploader {
        val (uploadId, fileName, accessToken) = params

        return services.completeLargeUpload(
            urlToUpload = UrlBuilder.completeUrl(),
            fileName = fileName,
            uploadId = uploadId,
            accessToken = accessToken
        )
    }

    override fun graphqlQuery() = ""

}
