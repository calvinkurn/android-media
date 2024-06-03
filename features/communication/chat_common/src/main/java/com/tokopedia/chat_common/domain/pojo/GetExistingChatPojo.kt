package com.tokopedia.chat_common.domain.pojo

import com.google.gson.annotations.SerializedName
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.BaseChatUiModel.Companion.SOURCE_AUTO_REPLY
import com.tokopedia.chat_common.data.parentreply.ParentReply

/**
 * @author by nisie on 10/12/18.
 */
data class GetExistingChatPojo(
    @SerializedName("chatReplies")
    val chatReplies: ChatReplies = ChatReplies()
)

data class ChatReplies(
    @SerializedName("minReplyTime")
    val minReplyTime: String = "0",

    @SerializedName("maxReplyTime")
    val maxReplyTime: String = "0",

    @SerializedName("contacts")
    val contacts: List<Contact> = ArrayList(),

    @SerializedName("list")
    var list: List<ChatRepliesItem> = ArrayList(),

    @SerializedName("hasNext")
    val hasNext: Boolean = false,

    @SerializedName("hasNextAfter")
    val hasNextAfter: Boolean = false,

    @SerializedName("textareaReply")
    val textAreaReply: Int = 0,

    @SerializedName("showTimeMachine")
    val showTimeMachine: Int = 0,

    @SerializedName("block")
    val block: Block = Block(),

    @SerializedName("replyIDsAttachment")
    val replyIDs: String = "",

    @SerializedName("attachmentIDs")
    var attachmentIds: String = ""
) {
    fun getInterlocutorContact(): Contact {
        return contacts.firstOrNull { contact -> contact.isInterlocutor } ?: Contact()
    }

    fun getSenderContact(): Contact {
        return contacts.firstOrNull { contact -> !contact.isInterlocutor } ?: Contact()
    }
}

data class Contact(
    @SerializedName("role")
    var role: String = "",

    @SerializedName("userId")
    val userId: String = "0",

    @SerializedName("shopId")
    val shopId: String = "0",

    @SerializedName("interlocutor")
    val isInterlocutor: Boolean = false,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("tag")
    val tag: String = "",

    @SerializedName("thumbnail")
    val thumbnail: String = "",

    @SerializedName("domain")
    val domain: String = "",

    @SerializedName("isOfficial")
    val isOfficial: Boolean = false,

    @SerializedName("isGold")
    val isGold: Boolean = false,

    @SerializedName("shopType")
    val shopType: Int = 0,

    @SerializedName("badge")
    val badge: String = "",

    @SerializedName("status")
    val status: Status = Status()
)

data class Status(
    @SerializedName("timestamp")
    val timestamp: Long = 0,

    @SerializedName("timestampStr")
    val timestampStr: String = "",

    @SerializedName("isOnline")
    val isOnline: Boolean = false
)

data class ChatRepliesItem(
    @SerializedName("date")
    val date: String = "",

    @SerializedName("chats")
    val chats: List<Chat> = ArrayList()
)

data class Chat(
    @SerializedName("time")
    val time: String = "",

    @SerializedName("replies")
    val replies: List<Reply> = ArrayList()
)

data class Reply(
    @SerializedName("msgId")
    val msgId: String = "0",

    @SerializedName("replyId")
    val replyId: String = "",

    @SerializedName("senderId")
    val senderId: String = "0",

    @SerializedName("senderName")
    val senderName: String = "",

    @SerializedName("role")
    val role: String = "",

    @SerializedName("fraudStatus")
    val fraudStatus: Int = 0,

    @SerializedName("msg")
    val msg: String = "",

    @SerializedName("replyTime")
    val replyTime: String = "",

    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("attachment")
    val attachment: Attachment = Attachment(),

    @SerializedName("isOpposite")
    val isOpposite: Boolean = false,

    @SerializedName("isHighlight")
    val isHighlight: Boolean = false,

    @SerializedName("isRead")
    val isRead: Boolean = true,

    @SerializedName("blastId")
    val blastId: String = "0",

    @SerializedName("source")
    val source: String = "",

    @SerializedName("label")
    val label: String = "",

    @SerializedName("parentReply")
    val parentReply: ParentReply? = null,

    @SerializedName("messageType")
    val messageType: String = ""
) {

    val attachmentType: Int get(): Int = attachment.type

    fun isAlsoProductAttachment(nextItem: Reply?): Boolean {
        return nextItem != null && isProductAttachment() && nextItem.isProductAttachment()
    }

    fun isAlsoTheSameBroadcast(nextItem: Reply?): Boolean {
        return nextItem != null && isBroadCast() && nextItem.isBroadCast() && blastId == nextItem.blastId
    }

    fun isProductAttachment(): Boolean {
        return attachment.type.toString() == AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
    }

    fun isAlsoVoucherAttachment(nextItem: Reply?): Boolean {
        return nextItem != null && isVoucherAttachment() && nextItem.isVoucherAttachment()
    }

    fun isVoucherAttachment(): Boolean {
        return attachment.type.toString() == AttachmentType.Companion.TYPE_VOUCHER
    }

    fun isBroadCast(): Boolean {
        return blastId != "0"
    }

    fun isAutoReply(): Boolean {
        return source == SOURCE_AUTO_REPLY
    }
}

data class Attachment(
    @SerializedName("id")
    var id: String = "",

    @SerializedName("type")
    val type: Int = 0,

    @SerializedName("attributes")
    var attributes: String = "",

    @SerializedName("fallback")
    val fallback: Fallback = Fallback()
)

data class Fallback(
    @SerializedName("message")
    val message: String = "",

    @SerializedName("html")
    val html: String = "",

    @SerializedName("url")
    var url: String = "",

    @SerializedName("span")
    var span: String = ""
)

data class Block(
    @SerializedName("isPromoBlocked")
    var isPromoBlocked: Boolean = false,

    @SerializedName("isBlocked")
    var isBlocked: Boolean = false,

    @SerializedName("blockedUntil")
    var blockedUntil: String = ""
)
