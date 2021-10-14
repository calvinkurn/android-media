package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.consts.UrlBuilder
import com.tokopedia.mediauploader.common.util.requestBody
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.VideoLargeUploader
import com.tokopedia.mediauploader.video.data.params.ChunkUploadParam
import kotlinx.coroutines.Dispatchers

class GetChunkUploaderUseCase constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<ChunkUploadParam, VideoLargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: ChunkUploadParam): VideoLargeUploader {
        val (sourceId, uploadId, partNumber, file, timeOut) = params

        return services.uploadLargeUpload(
            urlToUpload = UrlBuilder.uploadUrl(),
            sourceId = sourceId.requestBody(),
            uploadId = uploadId.requestBody(),
            partNumber = partNumber.requestBody(),
            videoFile = params.fileBody(),
            timeOut = timeOut
        )
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

}
