package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.VideoLargeUploader
import com.tokopedia.mediauploader.video.data.params.VideoUploaderParam
import com.tokopedia.mediauploader.video.data.params.VideoUploaderParam.Companion.urlChunkLargeUploadUrl
import kotlinx.coroutines.Dispatchers

class GetVideoUploaderUseCase constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<VideoUploaderParam, VideoLargeUploader>(Dispatchers.IO) {

    var progressCallback: ProgressCallback? = null

    override suspend fun execute(params: VideoUploaderParam): VideoLargeUploader {
        val uploadParam = params.LargeUpload()
        val videoFile = uploadParam.fileBlob(progressCallback)
        val sourceId = uploadParam.sourceId()
        val uploadId = uploadParam.uploadId()
        val partNumber = uploadParam.partNumber()

        return services.uploadLargeUpload(
            urlToUpload = urlChunkLargeUploadUrl(),
            uploadId = uploadId,
            sourceId = sourceId,
            partNumber = partNumber,
            videoFile = videoFile,
            timeOut = "600"
        )
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

}