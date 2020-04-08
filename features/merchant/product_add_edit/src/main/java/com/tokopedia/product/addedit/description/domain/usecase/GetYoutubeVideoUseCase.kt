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
        val YOUTUBE_API_KEY = intArrayOf(65, 73, 122, 97, 83, 121, 65, 68, 114, 110, 69,
                100, 74, 71, 119, 115, 86, 77, 49, 90, 54, 117, 87, 87, 110, 87, 65, 103, 90, 90,
                102, 49, 115, 83, 102, 110, 73, 86, 81 )
        const val YOUTUBE_LINK = "https://www.googleapis.com/youtube/v3/videos"
        const val ID = "id"
        const val KEY = "key"
        const val PART = "part"
        const val SNIPPET_CONTENT_DETAILS = "snippet,contentDetails"
    }

    private var videoId: String? = ""

    override suspend fun executeOnBackground(): Map<Type, RestResponse> {

        val restRequest = RestRequest.Builder(YOUTUBE_LINK, YoutubeVideoModel::class.java)
                .setQueryParams(queryParams = generateRequestParam())
                .setRequestType(RequestType.GET)
                .build()

        restRequestList.clear()
        restRequestList.add(restRequest)

        return repository.getResponses(restRequestList)
    }

    private fun generateRequestParam(): Map<String, Any> {
        val requestParamMap = mutableMapOf<String, Any>()
        requestParamMap[PART] = SNIPPET_CONTENT_DETAILS
        requestParamMap[ID] = videoId.toString()
        requestParamMap[KEY] = intArrayToString(YOUTUBE_API_KEY)
        return requestParamMap
    }

    fun setVideoId(videoId: String) {
        this.videoId = videoId
    }

    private fun intArrayToString(youtubeApiKey: IntArray): String {
        val str = StringBuilder()
        for (i in youtubeApiKey) {
            str.append(i.toChar())
        }
        return str.toString()
    }
}