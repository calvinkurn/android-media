package com.tokopedia.product.addedit.description.domain.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.product.addedit.description.presentation.model.youtube.YoutubeVideoModel
import java.lang.reflect.Type
import javax.inject.Inject

class GetYoutubeVideoUseCase @Inject constructor(
        private val repository: RestRepository
): RestRequestUseCase(repository) {

    companion object {
        const val YOUTUBE_API_KEY = "AIzaSyADrnEdJGwsVM1Z6uWWnWAgZZf1sSfnIVQ"
        const val YOUTUBE_LINK = "https://www.googleapis.com/youtube/v3/videos"
        const val ID = "id"
        const val KEY = "key"
        const val PART = "part"
        const val SNIPPET_CONTENT_DETAILS = "snippet,contentDetails"
    }

    private var videoId: String? = ""

    override suspend fun executeOnBackground(): Map<Type, RestResponse> {

        val restRequestList = mutableListOf<RestRequest>()

        val restRequest = RestRequest.Builder(YOUTUBE_LINK, YoutubeVideoModel::class.java)
                .setQueryParams(generateRequestParam())
                .setRequestType(RequestType.GET)
                .build()

        this.restRequestList.add(restRequest)

        return repository.getResponses(restRequestList)
    }

    private fun generateRequestParam(): Map<String, Any> {

        return mapOf<String, Any>().apply {
            PART to SNIPPET_CONTENT_DETAILS
            ID to videoId
            KEY to YOUTUBE_API_KEY
        }
    }

    fun setVideoId(videoId: String) {
        this.videoId = videoId
    }


}