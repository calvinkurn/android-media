package com.tokopedia.chat_common.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.websocket.WebSocketResponse

/**
 * @author : Steven 29/11/18
 */


interface BaseChatContract {
    interface View : BaseListViewListener<Visitable<*>>, CustomerView {

        fun onReceiveStartTypingEvent()

        fun onReceiveStopTypingEvent()

        fun onReceiveReadEvent()

        fun onReceiveMessageEvent(visitable: Visitable<*>)

    }

    interface Presenter<V : CustomerView> : CustomerPresenter<V> {
        fun mappingEvent(webSocketResponse: WebSocketResponse, messageId: String)

        fun mapToVisitable(pojo: ChatSocketPojo): Visitable<*>

        fun sendMessage(messageId: String, sendMessage: String,
                        startTime: String, opponentId: String,
                        onSendingMessage : () -> Unit)

    }
}