package com.tokopedia.topchat.chatlist.listener

import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.usecase.coroutines.Success

/**
 * @author : Steven 2019-08-06
 */
interface ChatListWebSocketContract {
    interface Activity {
        fun notifyViewCreated()
    }

    interface Fragment
}