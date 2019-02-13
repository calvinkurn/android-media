package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.kotlin.view.adapter.chatroom.typefactory.GroupChatTypeFactory

/**
 * @author by nisie on 2/22/18.
 */

class UserActionViewModel(val userId: String, val userName: String, val avatarUrl: String, val actionType: Int) : BaseChatViewModel("", System.currentTimeMillis(), 0, ""), Visitable<GroupChatTypeFactory> {

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        val ACTION_ENTER = 1
        val ACTION_EXIT = 2
    }
}
