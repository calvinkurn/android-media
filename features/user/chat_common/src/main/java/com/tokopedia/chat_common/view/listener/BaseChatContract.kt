package com.tokopedia.chat_common.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo

/**
 * @author : Steven 29/11/18
 */


interface BaseChatContract {
    interface View : BaseListViewListener<Visitable<*>> {
        fun getMsgId(): String

        fun getStringResource(str: Int)

        fun getNetworkMode(): Int

        fun onSuccessGetChat(model: ArrayList<Visitable<*>>)

        fun onReceiveStartTypingEvent()

        fun onReceiveStopTypingEvent()

        fun onReceiveReadEvent()

        fun onReceiveMessageEvent(visitable: Visitable<*>)

        fun disableAction()

        fun showSnackbarError(string: Unit)

        fun addDummyMessage(visitable: Visitable<*>)

        fun removeDummy(visitable: Visitable<*>)

        fun clearEditText()

    }
    interface Presenter : CustomerPresenter<View> {

        fun getChatUseCase(messageId : String, onError : (Exception) -> Unit)

        fun mapToVisitable(pojo: ChatSocketPojo): Visitable<*>

        fun sendMessage(sendMessage: String)

    }
}