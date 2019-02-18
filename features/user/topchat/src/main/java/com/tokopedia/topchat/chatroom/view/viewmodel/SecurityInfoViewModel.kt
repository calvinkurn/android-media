package com.tokopedia.topchat.chatroom.view.viewmodel

import android.text.TextUtils

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatlist.data.TopChatUrl
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

/**
 * @author by nisie on 10/24/17.
 */

class SecurityInfoViewModel(url: String) : Visitable<TopChatTypeFactory> {

    var url = TopChatUrl.SECURITY_INFO_URL
        private set

    init {
        if (!TextUtils.isEmpty(url))
            this.url = url
    }

    override fun type(chatRoomTypeFactory: TopChatTypeFactory): Int {
        return chatRoomTypeFactory.type(this)
    }
}
