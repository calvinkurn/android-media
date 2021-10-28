package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.consts.UrlBuilder
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.data.state.TranscodingState
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetTranscodingStatusUseCase @Inject constructor(
    private val services: VideoUploadServices
) : CoroutineUseCase<String, TranscodingState>(Dispatchers.IO) {

    override suspend fun execute(params: String): TranscodingState {
        if (params.isEmpty()) return TranscodingState.UNKNOWN

        val transcoding = services.checkTranscodingStatus(
            url = UrlBuilder.isTranscodeUrl(params)
        )

        return transcoding.status()
    }

    override fun graphqlQuery() = ""

}