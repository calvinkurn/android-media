package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.consts.UrlBuilder
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.util.requestBody
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.SimpleUploader
import com.tokopedia.mediauploader.video.data.params.SimpleUploadParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetSimpleUploaderUseCase @Inject constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<SimpleUploadParam, SimpleUploader>(Dispatchers.IO) {

    var progressUploader: ProgressUploader? = null

    override suspend fun execute(params: SimpleUploadParam): SimpleUploader {
        val (sourceId, file, timeOut) = params

        return services.simpleUpload(
            url = UrlBuilder.simpleUrl(sourceId),
            videoFile = params.fileBody(progressUploader),
            fileName = file.name.requestBody(),
            timeOut = timeOut
        )
    }

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

}