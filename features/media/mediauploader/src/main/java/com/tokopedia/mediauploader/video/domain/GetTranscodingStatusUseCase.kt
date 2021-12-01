package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.consts.UrlBuilder
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.entity.Transcoding
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetTranscodingStatusUseCase @Inject constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<String, Transcoding>(Dispatchers.IO) {

    override suspend fun execute(params: String): Transcoding {
        return services.checkTranscodingStatus(
            url = UrlBuilder.isTranscodeUrl(params)
        )
    }

    override fun graphqlQuery() = ""

}