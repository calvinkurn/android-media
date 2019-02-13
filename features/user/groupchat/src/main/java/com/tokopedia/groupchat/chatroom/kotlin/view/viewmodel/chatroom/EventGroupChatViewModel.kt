package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.kotlin.view.adapter.chatroom.typefactory.GroupChatTypeFactory

/**
 * @author : Steven 22/10/18
 */
class EventGroupChatViewModel(var isFreeze: Boolean, var isBanned: Boolean, channelId: String, userId: String) : Visitable<GroupChatTypeFactory> {
    var channelId: String
        internal set
    var userId: String
        internal set

    init {
        this.channelId = channelId
        this.userId = userId
    }

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return 0
    }
}
