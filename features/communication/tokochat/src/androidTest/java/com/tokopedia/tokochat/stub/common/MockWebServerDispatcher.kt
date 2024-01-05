package com.tokopedia.tokochat.stub.common

import com.tokopedia.tokochat.stub.common.util.ResponseReader
import com.tokopedia.tokochat.stub.domain.response.ApiResponseModelStub
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.Companion.CHANNEL_API
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.Companion.CHANNEL_ID_API
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.Companion.CHANNEL_LIST
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.Companion.CONNECTION_API
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.Companion.IMAGE_UPLOAD_URL_API
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.Companion.IMAGE_URL_API
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.Companion.MESSAGES
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.Companion.PROFILE_API
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub.Companion.SEND_MESSAGE_API
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
            /**
             * Chat Room
             */
            url.contains(CONNECTION_API) -> ApiResponseStub.getInstance().connectionResponse
            url.contains(PROFILE_API) -> ApiResponseStub.getInstance().profileResponse
            url.contains(CHANNEL_ID_API) -> {
                ApiResponseStub.getInstance().channelIdResponse
            }
            (url.contains(CHANNEL_API) && !url.contains(MESSAGES)) ->
                ApiResponseStub.getInstance().channelDetailsResponse
            (url.contains(CHANNEL_API) && url.contains(MESSAGES)) ->
                ApiResponseStub.getInstance().chatHistoryResponse
            (url.contains(IMAGE_URL_API)) -> {
                if (url.contains(IMAGE_UPLOAD_URL_API)) {
                    ApiResponseStub.getInstance().imageAttachmentUploadResponse
                } else {
                    ApiResponseStub.getInstance().imageAttachmentDownloadResponse
                }
            }
            (url.contains(SEND_MESSAGE_API)) -> ApiResponseStub.getInstance().sendMessageResponse

            /**
             * Chat List
             */
            url.contains(CHANNEL_LIST) -> {
                ApiResponseStub.getInstance().channelListResponse
            }
            else -> ApiResponseStub.getInstance().generalEmptyResponse
        }
    }

    private fun getStreamResponse(apiResponse: ApiResponseModelStub): String {
        val rawResponse = ResponseReader.convertJsonToStream(apiResponse.responseJsonPath)
        return apiResponse.responseEditor?.invoke(rawResponse) ?: rawResponse
    }
}
