package com.tokopedia.chat_common.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author : Steven 29/11/18
 */


interface BaseChatContract {
    interface View : BaseListViewListener<Visitable<*>> {

        fun onSuccessGetChat(model: ArrayList<Visitable<*>>)

        fun developmentView()

        fun onReceiveStartTypingEvent()

        fun onReceiveStopTypingEvent()

        fun onReceiveReadEvent()

        fun onReceiveMessageEvent(visitable: Visitable<*>)

    }
    interface Presenter : CustomerPresenter<View> {
        fun getChatUseCase(messageId : String)

        fun getChatUseCase(messageId : String, page: Int)

        fun mappingEvent(webSocketResponse: WebSocketResponse, messageId: String)

    }
}