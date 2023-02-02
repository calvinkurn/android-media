package com.tokopedia.tokochat.stub.common

import com.tokopedia.tokochat.stub.common.util.ResponseReader
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.CHANNEL_API
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.CHANNEL_ID_API
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.CONNECTION_API
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.IMAGE_URL_API
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.MESSAGES
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.PROFILE_API
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockWebServerDispatcher : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        return getMockResponse(request.path ?: "")
    }

    private fun getMockResponse(url: String): MockResponse {
        val pairResponse = getPairResponse(url)
        return MockResponse()
            .setResponseCode(pairResponse.first)
            .setBody(getStreamResponse(pairResponse.second))
    }

    private fun getPairResponse(url: String): Pair<Int, String> {
        return when {
            url.contains(CONNECTION_API) -> ApiResponseStub.connectionResponse
            url.contains(PROFILE_API) -> ApiResponseStub.profileResponse
            url.contains(CHANNEL_ID_API) -> {
                ApiResponseStub.channelIdResponse
            }
            (url.contains(CHANNEL_API) && !url.contains(MESSAGES)) ->
                ApiResponseStub.channelDetailsResponse
            (url.contains(CHANNEL_API) && url.contains(MESSAGES)) ->
                ApiResponseStub.chatHistoryResponse
            (url.contains(IMAGE_URL_API)) -> ApiResponseStub.imageAttachmentResponse
            else -> ApiResponseStub.generalEmptyResponse
        }
    }

    private fun getStreamResponse(fileName: String): String {
        return ResponseReader.convertJsonToStream(fileName)
    }
}
