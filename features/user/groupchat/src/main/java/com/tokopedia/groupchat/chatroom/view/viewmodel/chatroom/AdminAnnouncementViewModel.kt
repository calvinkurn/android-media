package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory

/**
 * @author by nisie on 2/7/18.
 */

class AdminAnnouncementViewModel(message: String, createdAt: Long, updatedAt: Long, messageId: String) : BaseChatViewModel(message, createdAt, updatedAt, messageId), Visitable<GroupChatTypeFactory> {

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}
