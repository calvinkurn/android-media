package com.tokopedia.chat_common.presenter

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_WEBSOCKET
import com.tokopedia.chat_common.domain.mapper.WebsocketMessageMapper
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.WebSocketResponse

/**
 * @author : Steven 29/11/18
 */

abstract class BaseChatPresenter<T : BaseChatContract.View> constructor(
        open var userSession: UserSessionInterface,
        open var websocketMessageMapper: WebsocketMessageMapper
) : BaseDaggerPresenter<T>(), BaseChatContract.Presenter<T> {

    protected var networkMode: Int = MODE_WEBSOCKET

    override fun mappingEvent(webSocketResponse: WebSocketResponse, messageId: String) {
        val pojo: ChatSocketPojo = Gson().fromJson(webSocketResponse.getData(), ChatSocketPojo::class.java)
        if (pojo.msgId.toString() != messageId) return

        when (webSocketResponse.getCode()) {
            EVENT_TOPCHAT_TYPING -> view.onReceiveStartTypingEvent()
            EVENT_TOPCHAT_END_TYPING -> view.onReceiveStopTypingEvent()
            EVENT_TOPCHAT_READ_MESSAGE -> view.onReceiveReadEvent()
            EVENT_TOPCHAT_REPLY_MESSAGE -> {
                view.onReceiveMessageEvent(mapToVisitable(pojo))
            }
        }
    }

    override fun mapToVisitable(pojo: ChatSocketPojo): Visitable<*> {
        return websocketMessageMapper.map(pojo)
    }

    abstract fun destroyWebSocket()


    override fun sendMessage(
            messageId: String,
            sendMessage: String,
            startTime: String,
            opponentId: String,
            onSendingMessage : () -> Unit
    ) {
        if (isValidReply(sendMessage)) {
            onSendingMessage()
            if (networkMode == MODE_WEBSOCKET) {
                sendMessageWithWebsocket(messageId, sendMessage, startTime, opponentId)
            } else {
                sendMessageWithApi(messageId, sendMessage, startTime)
            }
        } else {
            showErrorSnackbar((R.string.error_empty_product))
        }
    }

    private fun isValidReply(message: String) = message.isNotBlank()

    abstract fun sendMessageWithWebsocket(messageId: String, sendMessage: String, startTime: String, opponentId: String)

    abstract fun sendMessageWithApi(messageId: String, sendMessage: String, startTime: String)

    abstract fun showErrorSnackbar(stringId: Int)

    abstract fun isUploading(): Boolean
}
