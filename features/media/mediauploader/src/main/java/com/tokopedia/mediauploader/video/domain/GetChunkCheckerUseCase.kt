package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.consts.UrlBuilder
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import com.tokopedia.mediauploader.video.data.params.ChunkCheckerParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetChunkCheckerUseCase @Inject constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<ChunkCheckerParam, LargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: ChunkCheckerParam): LargeUploader {
        val (uploadId, partNumber, fileName) = params

        return services.chunkCheckerUpload(
            url = UrlBuilder.isChunkUrl(),
            fileName = fileName,
            uploadId = uploadId,
            partNumber = partNumber
        )
    }

    override fun graphqlQuery() = ""

}