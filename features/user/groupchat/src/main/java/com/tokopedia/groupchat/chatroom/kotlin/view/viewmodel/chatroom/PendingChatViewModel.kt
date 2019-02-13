package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom

/**
 * @author by nisie on 2/15/18.
 */

//class PendingChatViewModel : BaseChatViewModel, Visitable<GroupChatTypeFactory> {
//
//    internal var senderId: String
//    internal var senderName: String
//    internal var senderIconUrl: String
//    internal var isInfluencer: Boolean = false
//    var isRetry: Boolean = false
//
//    constructor(message: String, createdAt: Long, updatedAt: Long,
//                messageId: String, senderId: String, senderName: String,
//                senderIconUrl: String, isInfluencer: Boolean) : super(message, createdAt, updatedAt, messageId) {
//        this.senderId = senderId
//        this.senderName = senderName
//        this.senderIconUrl = senderIconUrl
//        this.isInfluencer = isInfluencer
//        this.isRetry = false
//    }
//
//    constructor(message: String, senderId: String, senderName: String, senderIconUrl: String, isInfluencer: Boolean) : super(message, 0, 0, "") {
//        this.senderId = senderId
//        this.senderName = senderName
//        this.senderIconUrl = senderIconUrl
//        this.isInfluencer = isInfluencer
//        this.isRetry = false
//    }
//
//    override fun type(typeFactory: GroupChatTypeFactory): Int {
//        return typeFactory.type(this)
//    }
//
//    override fun getSenderId(): String {
//        return senderId
//    }
//
//    override fun getSenderName(): String {
//        return senderName
//    }
//
//    override fun getSenderIconUrl(): String {
//        return senderIconUrl
//    }
//
//    override fun isInfluencer(): Boolean {
//        return isInfluencer
//    }
//}
