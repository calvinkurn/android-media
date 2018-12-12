package com.tokopedia.topchat.revamp.listener

import com.tokopedia.chat_common.view.listener.BaseChatContract

/**
 * @author : Steven 11/12/18
 */
interface TopChatContract {

    interface View : BaseChatContract.View {

        fun developmentView()

//        fun onSuccessLoadFirstTime(dummyList: ArrayList<Visitable<*>>)
    }

    interface Presenter : BaseChatContract.Presenter {

    }
}