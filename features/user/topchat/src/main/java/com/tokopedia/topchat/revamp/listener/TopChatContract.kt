package com.tokopedia.topchat.revamp.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.listener.BaseChatContract

/**
 * @author : Steven 11/12/18
 */
interface TopChatContract {

    interface View : BaseChatContract.View {

        fun developmentView()

        fun removeDummy(it : Visitable<*>)

//        fun onSuccessLoadFirstTime(dummyList: ArrayList<Visitable<*>>)
    }

    interface Presenter : BaseChatContract.Presenter<View> {
        fun connectWebSocket(messageId: String)

    }
}