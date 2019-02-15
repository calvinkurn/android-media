package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory

/**
 * @author by nisie on 2/27/18.
 */

class ImageAnnouncementViewModel(val contentImageUrl: String, createdAt: Long, updatedAt: Long,
                                 messageId: String, senderId: String, senderName: String,
                                 senderIconUrl: String, isInfluencer: Boolean,
                                 isAdministrator: Boolean, val redirectUrl: String) : BaseChatViewModel("", createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl, isInfluencer, isAdministrator), Visitable<GroupChatTypeFactory> {

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {

        const val ADMIN_ANNOUNCEMENT = "announcement"
    }
}
