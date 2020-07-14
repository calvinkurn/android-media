package com.tokopedia.youtube_common.domain.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import java.lang.reflect.Type
import javax.inject.Inject

class GetYoutubeVideoDetailUseCase @Inject constructor(
        private val repository: RestRepository
): RestRequestUseCase(repository) {

    companion object {
        val ENDPOINT_URL =  "${TokopediaUrl.getInstance().GOLDMERCHANT}youtube/v3/videos"
        const val ID_KEY = "id"
        const val PART_KEY = "part"
        const val PART_VALUE = "snippet"
    }

    private var videoId: String? = ""

    override suspend fun executeOnBackground(): Map<Type, RestResponse> {

        val restRequest = RestRequest.Builder(ENDPOINT_URL, YoutubeVideoDetailModel::class.java)
                .setQueryParams(queryParams = generateRequestParam())
                .setRequestType(RequestType.GET)
                .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    private fun generateRequestParam(): Map<String, Any> {
        val requestParamMap = mutableMapOf<String, Any>()
        requestParamMap[ID_KEY] = videoId.toString()
        requestParamMap[PART_KEY] = PART_VALUE
        return requestParamMap
    }

    fun setVideoId(videoId: String) {
        this.videoId = videoId
    }
}