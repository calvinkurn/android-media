package com.tokopedia.chat_common.domain.pojo

/**
 * @author by nisie on 10/12/18.
 */
open
data class GetExistingChatPojo(
        val chatReplies: ChatReplies
)

data class ChatReplies(
        val contacts: List<Contact> = ArrayList(),
        val list: List<ChatRepliesItem> = ArrayList()
)

data class Contact(
        val role: String = "",
        val userId: Int = 0,
        val shopId: Int = 0,
        val interlocutor: Boolean = false,
        val name: String = "",
        val tag: String = "",
        val thumbnail: String = "",
        val domain: String = ""
)

data class ChatRepliesItem(
        val date: String = "",
        val chats: List<Chat> = ArrayList()
)

data class Chat(
        val time: String  = "",
        val replies: List<Reply> = ArrayList()
)

data class Reply(
        val msgId: Int = 0,
        val replyId: Int = 0,
        val senderId: Int = 0,
        val senderName: String = "",
        val role: String = "",
        val msg: String = "",
        val replyTime: String = "",
        val status: Int = 0,
        val attachmentID: Int = 0,
        val attachment: Attachment?,
        val isOpposite: Boolean = false,
        val isHighlight: Boolean = false,
        val isRead: Boolean = true
)

data class Attachment(
        val id: Int = 0,
        val type: Int = 0,
        val attributes: Attributes?,
        val fallback: Fallback?
)

data class Attributes(
        val url: Any
)

data class Fallback(
        val message: String = "",
        val html: String = ""
)