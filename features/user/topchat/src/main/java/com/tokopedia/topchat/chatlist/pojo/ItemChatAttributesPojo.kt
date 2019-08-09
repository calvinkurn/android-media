package com.tokopedia.topchat.chatlist.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.chat_common.domain.pojo.ChatItemPojo
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant

/**
 * @author : Steven 2019-08-08
 */
data class ItemChatAttributesPojo(
        @SerializedName("lastReplyMessage")
        @Expose
        var lastReplyMessage: String = "",
        @SerializedName("readStatus")
        @Expose
        var readStatus: Int,
        @SerializedName("unreads")
        @Expose
        var unreads: Int,
        @SerializedName("fraudStatus")
        @Expose
        var fraudStatus: Int,
        @SerializedName("contact")
        @Expose
        var contact: ItemChatAttributesContactPojo?

) {
        constructor(lastReplyMessage: String, contact: ItemChatAttributesContactPojo?)
                :this(lastReplyMessage, ChatItemListViewHolder.STATE_CHAT_UNREAD, 0, 0, contact)
}