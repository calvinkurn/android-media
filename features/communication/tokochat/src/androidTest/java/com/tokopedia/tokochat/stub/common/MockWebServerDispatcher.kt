package com.tokopedia.tokochat.stub.common

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockWebServerDispatcher: Dispatcher() {

    var responseCode: Int = 200

    override fun dispatch(request: RecordedRequest): MockResponse {
        return MockResponse()
            .setResponseCode(responseCode)
            .setBody(
                ResponseReader.convertJsonToStream(getResponseString(request.path?: ""))
            )
    }

    private fun getResponseString(url: String): String {
        return if (responseCode == 200) {
            getSuccessResponseString(url)
        } else {
            "//TODO"
        }
    }

    private fun getSuccessResponseString(url: String): String {
        return when {
            url.contains("v2/chat/connection") -> "connection/success_get_connection.json"
            url.contains("/v1/chat/profile") -> "profile/success_get_profile.json"
            url.contains("/v2/order") -> "channel_details/success_get_channel_id.json"
            (url.contains("/v2/chat/channels") && !url.contains("messages")) ->
                "channel_details/success_get_channel_details.json"
            (url.contains("/v2/chat/channels") && url.contains("messages")) ->
                "chat_history/success_get_chat_history.json"
            else -> ""
        }
    }
}
