package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.MediaUploaderUrl
import com.tokopedia.mediauploader.common.util.network.requestBody
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.LargeUploader
import com.tokopedia.mediauploader.video.data.params.ChunkUploadParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetChunkUploaderUseCase @Inject constructor(
    private val services: VideoUploadServices,
    private val url: MediaUploaderUrl
) : CoroutineUseCase<ChunkUploadParam, LargeUploader>(Dispatchers.IO) {

    override suspend fun execute(params: ChunkUploadParam): LargeUploader {
        val (sourceId, uploadId, partNumber, _, _, timeOut) = params

        return services.uploadLargeUpload(
            url = url.largePart(),
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
