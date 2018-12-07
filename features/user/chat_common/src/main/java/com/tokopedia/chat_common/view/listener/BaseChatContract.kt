package com.tokopedia.chat_common.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author : Steven 29/11/18
 */


interface BaseChatContract {
    interface View : BaseListViewListener<Visitable<*>> {
        fun getMessageId(): String?

        fun onSuccessGetChat(model: ArrayList<Visitable<*>>)

        fun developmentView()

        fun receiveStartTypingEvent()

        fun receiveStopTypingEvent()

        fun receiveReadEvent()

        fun receiveMessageEvent(visitable: Visitable<*>)

    }
    interface Presenter : CustomerPresenter<View> {
        fun getChatUseCase(messageId : String)

        fun getChatUseCase(messageId : String, page: Int)

    }
}