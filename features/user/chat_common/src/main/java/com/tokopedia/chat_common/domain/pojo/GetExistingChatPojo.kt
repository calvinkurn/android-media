package com.tokopedia.chat_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.chat_common.data.AttachmentType

/**
 * @author by nisie on 10/12/18.
 */
data class GetExistingChatPojo(
        @Expose
        @SerializedName("chatReplies")
        val chatReplies: ChatReplies = ChatReplies()
)

data class ChatReplies(
        @Expose
        @SerializedName("minReplyTime")
        val minReplyTime: String = "0",
        @Expose
        @SerializedName("maxReplyTime")
        val maxReplyTime: String = "0",
        @Expose
        @SerializedName("contacts")
        val contacts: List<Contact> = ArrayList(),
        @Expose
        @SerializedName("list")
        val list: List<ChatRepliesItem> = ArrayList(),
        @Expose
        @SerializedName("hasNext")
        val hasNext: Boolean = false,
        @Expose
        @SerializedName("hasNextAfter")
        val hasNextAfter: Boolean = false,
        @Expose
        @SerializedName("attachmentIDs")
        val attachmentIds: String = "",
        @Expose
        @SerializedName("textareaReply")
        val textAreaReply: Int = 0,
        @Expose
        @SerializedName("showTimeMachine")
        val showTimeMachine: Int = 0,
        @Expose
        @SerializedName("block")
        val block: Block = Block()
)

data class Contact(
        @Expose
        @SerializedName("role")
        val role: String = "",
        @Expose
        @SerializedName("userId")
        val userId: Long = 0L,
        @Expose
        @SerializedName("shopId")
        val shopId: Long = 0L,
        @Expose
        @SerializedName("interlocutor")
        val isInterlocutor: Boolean = false,
        @Expose
        @SerializedName("name")
        val name: String = "",
        @Expose
        @SerializedName("tag")
        val tag: String = "",
        @Expose
        @SerializedName("thumbnail")
        val thumbnail: String = "",
        @Expose
        @SerializedName("domain")
        val domain: String = "",
        @Expose
        @SerializedName("isOfficial")
        val isOfficial: Boolean = false,
        @Expose
        @SerializedName("isGold")
        val isGold: Boolean = false,
        @Expose
        @SerializedName("badge")
        val badge: String = "",
        @Expose
        @SerializedName("status")
        val status: Status = Status()
)

data class Status(
        @Expose
        @SerializedName("timestamp")
        val timestamp: Long = 0,
        @Expose
        @SerializedName("timestampStr")
        val timestampStr: String = "",
        @Expose
        @SerializedName("isOnline")
        val isOnline: Boolean = false
)

data class ChatRepliesItem(
        @Expose
        @SerializedName("date")
        val date: String = "",
        @Expose
        @SerializedName("chats")
        val chats: List<Chat> = ArrayList()
)

data class Chat(
        @Expose
        @SerializedName("time")
        val time: String = "",
        @Expose
        @SerializedName("replies")
        val replies: List<Reply> = ArrayList()
)

data class Reply(
        @Expose
        @SerializedName("msgId")
        val msgId: Long = 0,
        @Expose
        @SerializedName("replyId")
        val replyId: String = "",
        @Expose
        @SerializedName("senderId")
        val senderId: Long = 0,
        @Expose
        @SerializedName("senderName")
        val senderName: String = "",
        @Expose
        @SerializedName("role")
        val role: String = "",
        @Expose
        @SerializedName("fraudStatus")
        val fraudStatus: Int = 0,
        @Expose
        @SerializedName("msg")
        val msg: String = "",
        @Expose
        @SerializedName("replyTime")
        val replyTime: String = "",
        @Expose
        @SerializedName("status")
        val status: Int = 0,
        @Expose
        @SerializedName("attachment")
        val attachment: Attachment = Attachment(),
        @Expose
        @SerializedName("isOpposite")
        val isOpposite: Boolean = false,
        @Expose
        @SerializedName("isHighlight")
        val isHighlight: Boolean = false,
        @Expose
        @SerializedName("isRead")
        val isRead: Boolean = true,
        @Expose
        @SerializedName("blastId")
        val blastId: Long = 0,
        @Expose
        @SerializedName("source")
        val source: String = ""
) {

    val attachmentType: Int get(): Int = attachment?.type ?: 0

    fun isAlsoProductAttachment(nextItem: Reply?): Boolean {
        return nextItem != null && isProductAttachment() && nextItem.isProductAttachment()
    }

    fun isAlsoTheSameBroadcast(nextItem: Reply?): Boolean {
        return nextItem != null && isBroadCast() && nextItem.isBroadCast() && blastId == nextItem.blastId
    }

    fun isProductAttachment(): Boolean {
        return attachment?.type.toString() == AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
    }

    fun isBroadCast(): Boolean {
        return blastId > 0
    }

}

data class Attachment(
        @Expose
        @SerializedName("id")
        val id: String = "",
        @Expose
        @SerializedName("type")
        val type: Int = 0,
        @Expose
        @SerializedName("attributes")
        var attributes: String = "",
        @Expose
        @SerializedName("fallback")
        val fallback: Fallback = Fallback()
)

data class Fallback(
        @Expose
        @SerializedName("message")
        val message: String = "",
        @Expose
        @SerializedName("html")
        val html: String = "",
        @SerializedName("url")
        @Expose
        var url: String = "",
        @SerializedName("span")
        @Expose
        var span: String = ""
)


data class Block(
        @Expose
        @SerializedName("isPromoBlocked")
        var isPromoBlocked: Boolean = false,
        @Expose
        @SerializedName("isBlocked")
        val isBlocked: Boolean = false,
        @Expose
        @SerializedName("blockedUntil")
        val blockedUntil: String = ""
)