package com.tokopedia.tokochat.stub.domain.response

import com.tokopedia.tokochat.domain.response.background.TokoChatBackgroundResponse
import com.tokopedia.tokochat.domain.response.orderprogress.TokoChatOrderProgressResponse
import com.tokopedia.tokochat.domain.response.ticker.TokochatRoomTickerResponse
import com.tokopedia.tokochat.stub.common.util.AndroidFileUtil
import java.lang.reflect.Type

object GqlResponseStub {
    var chatBackgroundResponse = ResponseStub<TokoChatBackgroundResponse>(
        filePath = "chat_background/success_get_chat_background.json",
        type = TokoChatBackgroundResponse::class.java,
        query = "query tokoChatBackground",
        isError = false
    )

    var chatFirstTickerResponse = ResponseStub<TokochatRoomTickerResponse>(
        filePath = "ticker/success_get_first_ticker.json",
        type = TokochatRoomTickerResponse::class.java,
        query = "query getTokochatRoomTicker",
        isError = false
    )

    var chatOrderHistoryResponse = ResponseStub<TokoChatOrderProgressResponse>(
        filePath = "order_history/success_get_order_history.json",
        type = TokoChatOrderProgressResponse::class.java,
        query = "query getTokochatOrderProgress",
        isError = false
    )

    fun reset() {
        chatBackgroundResponse = ResponseStub(
            filePath = "chat_background/success_get_chat_background.json",
            type = TokoChatBackgroundResponse::class.java,
            query = "query tokoChatBackground",
            isError = false
        )

        chatFirstTickerResponse = ResponseStub(
            filePath = "ticker/success_get_first_ticker.json",
            type = TokochatRoomTickerResponse::class.java,
            query = "query getTokochatRoomTicker",
            isError = false
        )

        chatOrderHistoryResponse = ResponseStub(
            filePath = "order_history/success_get_order_history.json",
            type = TokoChatOrderProgressResponse::class.java,
            query = "query getTokochatOrderProgress",
            isError = false
        )
    }
}

data class ResponseStub<T> (
    var filePath: String = "",
    var type: Type,
    var query: String = "",
    var isError: Boolean = false
) {
    fun getResponseObject(): T {
        return AndroidFileUtil.parse(filePath, type)
    }
}
