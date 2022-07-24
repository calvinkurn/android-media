package com.tokopedia.topchat.chatlist.domain.pojo

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.chatlist.view.adapter.typefactory.ChatListTypeFactory
import com.tokopedia.topchat.chatlist.view.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatlist.view.adapter.viewholder.ChatItemListViewHolder.Companion.BUYER_TAG
import com.tokopedia.topchat.chatlist.view.adapter.viewholder.ChatItemListViewHolder.Companion.OFFICIAL_TAG
import com.tokopedia.topchat.chatlist.view.adapter.viewholder.ChatItemListViewHolder.Companion.SELLER_TAG
import com.tokopedia.topchat.chatlist.view.adapter.viewholder.ChatItemListViewHolder.Companion.STATE_CHAT_READ
import com.tokopedia.topchat.chatlist.view.adapter.viewholder.ChatItemListViewHolder.Companion.STATE_CHAT_UNREAD

/**
 * @author : Steven 2019-08-08
 */
data class ItemChatListPojo(
    @SerializedName("msgID")
    var msgId: String = "",

    @SerializedName("attributes")
    var attributes: ItemChatAttributesPojo?,

    @SerializedName("messageKey")
    var messageKey: String = ""
) : Visitable<ChatListTypeFactory> {

    val label: String get() = attributes?.label ?: ""
    val tag: String get() = attributes?.contact?.tag ?: ""
    val lastReplyTime: Long get() = attributes?.lastReplyTimestamp ?: 0
    val lastReplyTimeStr: String get() = attributes?.lastReplyTimeStr ?: ""
    val lastReplyTimeMillis: Long get() = lastReplyTimeStr.toLongOrZero()
    val lastReplyMessage: String get() = attributes?.lastReplyMessage ?: ""
    val thumbnail: String get() = attributes?.contact?.thumbnail ?: ""
    val name: String get() = attributes?.contact?.contactName ?: ""
    val isPinned: Boolean get() = attributes?.pinStatus == 1
    val totalUnread: String get() = attributes?.unreadReply?.toString() ?: ""
    var isActive: Boolean = false
        private set

    override fun type(typeFactory: ChatListTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun hasLabel(): Boolean {
        return label.isNotEmpty()
    }

    fun hasUnreadItem(): Boolean {
        attributes?.let {
            return it.readStatus == ChatItemListViewHolder.STATE_CHAT_UNREAD
        }
        return false
    }

    fun ids() = listOf(msgId)

    fun isUnread(): Boolean {
        return attributes?.readStatus == STATE_CHAT_UNREAD
    }

    fun isRead(): Boolean {
        return attributes?.readStatus == STATE_CHAT_READ
    }

    fun markAsRead() {
        attributes?.readStatus = STATE_CHAT_READ
        attributes?.unreadReply = 0
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

    fun isReplyTopBot(): Boolean {
        return attributes?.isReplyByTopbot == true
    }

    fun updatePinStatus(isPinChat: Boolean) {
        val pinStatus = if (isPinChat) 1 else 0
        attributes?.pinStatus = pinStatus
    }

    fun markAsActive() {
        this.isActive = true
    }

    fun markAsInactive() {
        this.isActive = false
    }

}