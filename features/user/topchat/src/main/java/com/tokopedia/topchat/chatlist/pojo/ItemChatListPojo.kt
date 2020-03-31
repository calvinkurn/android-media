package com.tokopedia.topchat.chatlist.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactory
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder.Companion.BUYER_TAG
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder.Companion.OFFICIAL_TAG
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder.Companion.SELLER_TAG
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder.Companion.STATE_CHAT_READ
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder.Companion.STATE_CHAT_UNREAD

/**
 * @author : Steven 2019-08-08
 */
data class ItemChatListPojo(
        @SerializedName("msgID")
        @Expose
        var msgId: String = "",
        @SerializedName("attributes")
        @Expose
        var attributes: ItemChatAttributesPojo?,
        @SerializedName("messageKey")
        @Expose
        var messageKey: String = ""
) : Visitable<ChatListTypeFactory>{
    val tag: String get() = attributes?.contact?.tag ?: ""
    val lastReplyTimeStr: String get() = attributes?.lastReplyMessage ?: ""
    val lastReplyMessage: String get() = attributes?.lastReplyMessage ?: ""
    val thumbnail: String get() = attributes?.contact?.thumbnail ?: ""
    val name: String get() = attributes?.contact?.contactName ?: ""
    val isPinned: Boolean get() = attributes?.pinStatus == 1
    val totalUnread: String get() = attributes?.unreadReply?.toString() ?: ""

    override fun type(typeFactory: ChatListTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun hasUnreadItem(): Boolean {
        attributes?.let {
            return it.readStatus == ChatItemListViewHolder.STATE_CHAT_UNREAD
        }
        return false
    }

    fun ids() = listOf(msgId)

    fun isUnread(): Boolean {
        return attributes?.readStatus == ChatItemListViewHolder.STATE_CHAT_UNREAD
    }

    fun markAsRead() {
        attributes?.readStatus = ChatItemListViewHolder.STATE_CHAT_READ
    }

    fun hasTheSameMsgId(state: ChatStateItem): Boolean {
        return msgId == state.msgID.toString()
    }

    fun getLiteralReadStatus(): String {
        return when (attributes?.readStatus) {
            STATE_CHAT_UNREAD -> "unread"
            STATE_CHAT_READ -> "read"
            else -> ""
        }
    }

    fun getLiteralUserType(): String {
        return when (attributes?.contact?.tag) {
            BUYER_TAG -> "buyer"
            SELLER_TAG -> "seller"
            OFFICIAL_TAG -> "OA"
            else -> ""
        }
    }

}