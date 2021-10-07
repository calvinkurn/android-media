package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.VideoLargeUploader
import com.tokopedia.mediauploader.video.data.params.VideoUploaderParam
import com.tokopedia.mediauploader.video.data.params.VideoUploaderParam.Companion.urlCheckChunkLargeUploadUrl
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetChunkCheckerUseCase @Inject constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<VideoUploaderParam, VideoLargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: VideoUploaderParam): VideoLargeUploader {
        val fileName = params.file.name
        val uploadId = params.uploadId
        val partNumber = params.partNumber

        return services.
        isValidChunkLargeUpload(
            urlToUpload = urlCheckChunkLargeUploadUrl(),
            fileName = fileName,
            uploadId = uploadId,
            partNumber = partNumber
        )
    }

    override fun graphqlQuery() = ""

}