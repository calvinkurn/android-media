package com.tokopedia.tokochat.stub.common

import com.tokopedia.tokochat.test.TokoChatTest
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response

class ConversationMockInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val responseString = getResponseString(chain.request().url.toString())
        return chain.proceed(chain.request())
            .newBuilder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message(responseString)
            .addHeader("content-type", "application/json")
            .build()
    }

    private fun getResponseString(url: String): String {
        return when {
            url.contains("/connection") -> responseConnection
            url.contains("/v2/order") -> responseChannelDetails
            else -> throw Throwable("API is not included")
        }
    }

    companion object {
        val responseConnection = """
            {
              "success": true,
              "data": {
                "id": "testId1",
                "sendbird_id": "testSendBirdId1",
                "sendbird_token": "testSendBirdToken1",
                "phone": "testPhone1"
              },
              "errors": null
            }
        """.trimIndent()

        val responseChannelDetails = """
            {
              "success": true,
              "data": {
                "channel_id": "${TokoChatTest.CHANNEL_ID_DUMMY}",
                "chat_client": "testChatClient1"
              },
              "errors": null
            }
        """.trimIndent()
    }
}
