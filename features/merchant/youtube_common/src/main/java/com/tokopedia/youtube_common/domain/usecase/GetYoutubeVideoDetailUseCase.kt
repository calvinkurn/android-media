package com.tokopedia.youtube_common.domain.usecase

import android.net.Uri
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.network.authentication.HEADER_USER_AGENT
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.network.exception.MessageErrorException
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

    companion object {
        const val KEY_YOUTUBE_VIDEO_ID = "v"
        const val WEB_PREFIX_HTTP = "http://"
        const val WEB_PREFIX_HTTPS = "https://"
    }

    private var videoId: String? = ""

    override suspend fun executeOnBackground(): Map<Type, RestResponse> {
        val restRequest = RestRequest.Builder(ENDPOINT_URL, YoutubeVideoDetailModel::class.java)
            .setQueryParams(queryParams = generateRequestParam())
            .setRequestType(RequestType.GET)
            .setHeaders(
                mapOf(
                    HEADER_USER_AGENT to AuthHelper.getUserAgent()
                )
            ).build()
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

    fun setVideoUrl(videoUrl: String) {
        try {
            var webVideoUrl = if (videoUrl.startsWith(WEB_PREFIX_HTTP) || videoUrl.startsWith(WEB_PREFIX_HTTPS)) {
                videoUrl
            } else {
                WEB_PREFIX_HTTPS + videoUrl
            }
            webVideoUrl = webVideoUrl.replace("(www\\.|m\\.)".toRegex(), "")

            val uri = Uri.parse(webVideoUrl)
            val videoId = when {
                uri.host == "youtu.be" -> uri.lastPathSegment
                uri.host == "youtube.com" -> uri.getQueryParameter(KEY_YOUTUBE_VIDEO_ID)
                uri.host == "www.youtube.com" -> uri.getQueryParameter(KEY_YOUTUBE_VIDEO_ID)
                else -> throw MessageErrorException("Unknown youtube url $webVideoUrl.")
            }
            setVideoId(videoId ?: "")
        } catch (e: NullPointerException) {
            throw MessageErrorException(e.message)
        }
    }
}