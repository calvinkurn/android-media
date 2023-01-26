package com.tokopedia.tokochat.stub.common

import com.tokopedia.tokochat.stub.common.util.ResponseReader
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
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
            url.contains("v2/chat/connection") -> ApiResponseStub.connectionResponse
            url.contains("/v1/chat/profile") -> ApiResponseStub.profileResponse
            url.contains("/v2/order") -> {
                ApiResponseStub.channelIdResponse
            }
            (url.contains("/v2/chat/channels") && !url.contains("messages")) ->
                ApiResponseStub.channelDetailsResponse
            (url.contains("/v2/chat/channels") && url.contains("messages")) ->
                ApiResponseStub.chatHistoryResponse
            (url.contains("/v1/image")) -> ApiResponseStub.imageAttachmentResponse
            else -> ApiResponseStub.generalEmptyResponse
        }
    }

    private fun getStreamResponse(fileName: String): String {
        return ResponseReader.convertJsonToStream(fileName)
    }
}
