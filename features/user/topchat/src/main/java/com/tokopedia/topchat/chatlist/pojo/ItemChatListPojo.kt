package com.tokopedia.topchat.chatlist.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactory
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder

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
}