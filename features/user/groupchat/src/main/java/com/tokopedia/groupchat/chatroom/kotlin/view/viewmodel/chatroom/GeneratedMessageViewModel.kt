package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.kotlin.view.adapter.chatroom.typefactory.GroupChatTypeFactory

/**
 * @author by nisie on 3/29/18.
 */

class GeneratedMessageViewModel(message: String, createdAt: Long, updatedAt: Long,
                                messageId: String, senderId: String, senderName: String,
                                senderIconUrl: String, isInfluencer: Boolean, isAdministrator: Boolean) : BaseChatViewModel(message, createdAt, updatedAt, messageId, senderId, senderName, senderIconUrl, isInfluencer, isAdministrator), Visitable<GroupChatTypeFactory> {

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {

        val TYPE = "generated_msg"
    }
}
