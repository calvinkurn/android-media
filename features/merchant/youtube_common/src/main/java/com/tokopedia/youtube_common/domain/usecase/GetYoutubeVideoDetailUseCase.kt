package com.tokopedia.youtube_common.domain.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.youtube_common.YoutubeCommonConstant.ENDPOINT_URL
import com.tokopedia.youtube_common.YoutubeCommonConstant.ID_KEY
import com.tokopedia.youtube_common.YoutubeCommonConstant.PART_KEY
import com.tokopedia.youtube_common.YoutubeCommonConstant.PART_VALUE
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import java.lang.reflect.Type
import javax.inject.Inject

class GetYoutubeVideoDetailUseCase @Inject constructor(
        private val repository: RestRepository
): RestRequestUseCase(repository) {

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

    fun setVideoId(videoId: String) {
        this.videoId = videoId
    }

    private fun generateRequestParam(): Map<String, Any> {
        val requestParamMap = mutableMapOf<String, Any>()
        requestParamMap[ID_KEY] = videoId.toString()
        requestParamMap[PART_KEY] = PART_VALUE
        return requestParamMap
    }
}