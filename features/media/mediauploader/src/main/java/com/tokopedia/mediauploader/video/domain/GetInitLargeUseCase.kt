package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.VideoLargeUploader
import com.tokopedia.mediauploader.video.data.params.VideoUploaderParam
import com.tokopedia.mediauploader.video.data.params.VideoUploaderParam.Companion.urlInitLargeUploadUrl
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetInitLargeUseCase @Inject constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<VideoUploaderParam, VideoLargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: VideoUploaderParam): VideoLargeUploader {
        val sourceId = params.sourceId
        val fileName = params.file.name

        return services.initLargeUpload(
            urlToUpload = urlInitLargeUploadUrl(),
            fileName = fileName,
            sourceId = sourceId
        )
    }

    override fun graphqlQuery() = ""

}