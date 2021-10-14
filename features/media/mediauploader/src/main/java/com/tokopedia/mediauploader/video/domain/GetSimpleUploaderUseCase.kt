package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.consts.UrlBuilder
import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.common.util.requestBody
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.VideoUploader
import com.tokopedia.mediauploader.video.data.params.SimpleUploadParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetSingleUploaderUseCase @Inject constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<SimpleUploadParam, VideoUploader>(Dispatchers.IO) {

    var progressCallback: ProgressCallback? = null

    override suspend fun execute(params: SimpleUploadParam): VideoUploader {
        val (sourceId, file, timeOut) = params

        return services.simpleUpload(
            urlToUpload = UrlBuilder.simpleUrl(sourceId),
            videoFile = params.fileBody(progressCallback),
            fileName = file.name.requestBody(),
            timeOut = timeOut
        )
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

}