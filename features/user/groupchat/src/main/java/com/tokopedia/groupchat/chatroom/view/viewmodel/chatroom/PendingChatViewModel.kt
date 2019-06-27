package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory

/**
 * @author by nisie on 2/15/18.
 */

class PendingChatViewModel : BaseChatViewModel, Visitable<GroupChatTypeFactory> {

    var isRetry: Boolean = false

    constructor(message: String, createdAt: Long, updatedAt: Long,
                messageId: String, senderId: String, senderName: String,
                senderIconUrl: String, isInfluencer: Boolean) : super(message, createdAt, updatedAt, messageId) {
        this.senderId = senderId
        this.senderName = senderName
        this.senderIconUrl = senderIconUrl
        this.isInfluencer = isInfluencer
        this.isRetry = false
    }

    constructor(message: String, senderId: String, senderName: String, senderIconUrl: String, isInfluencer: Boolean) : super(message, 0, 0, "") {
        this.senderId = senderId
        this.senderName = senderName
        this.senderIconUrl = senderIconUrl
        this.isInfluencer = isInfluencer
        this.isRetry = false
    }

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}
