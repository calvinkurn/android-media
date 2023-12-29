package com.tokopedia.inbox.universalinbox.stub.common

import com.tokopedia.inbox.universalinbox.stub.common.util.ResponseReader
import com.tokopedia.inbox.universalinbox.stub.data.response.ApiResponseModelStub
import com.tokopedia.inbox.universalinbox.stub.data.response.ApiResponseStub
import com.tokopedia.inbox.universalinbox.stub.data.response.ApiResponseStub.CHANNEL_LIST
import com.tokopedia.inbox.universalinbox.stub.data.response.ApiResponseStub.CONNECTION_API
import com.tokopedia.inbox.universalinbox.stub.data.response.ApiResponseStub.PROFILE_API
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockWebServerDispatcher : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        return getMockResponse(request.path ?: "")
    }

    private fun getMockResponse(url: String): MockResponse {
        val apiResponse = getApiResponse(url)
        return MockResponse()
            .setResponseCode(apiResponse.responseCode)
            .setBody(getStreamResponse(apiResponse))
    }

    private fun getApiResponse(url: String): ApiResponseModelStub {
        return when {
            url.contains(CONNECTION_API) -> ApiResponseStub.connectionResponse
            url.contains(PROFILE_API) -> ApiResponseStub.profileResponse
            url.contains(CHANNEL_LIST) -> {
                ApiResponseStub.channelListResponse
            }
            else -> ApiResponseStub.generalEmptyResponse
        }
    }

    private fun getStreamResponse(apiResponse: ApiResponseModelStub): String {
        val rawResponse = ResponseReader.convertJsonToStream(apiResponse.responseJsonPath)
        return apiResponse.responseEditor?.invoke(rawResponse) ?: rawResponse
    }
}
