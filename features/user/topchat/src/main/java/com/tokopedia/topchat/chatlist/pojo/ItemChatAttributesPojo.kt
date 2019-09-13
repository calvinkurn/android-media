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
        @SerializedName("lastReplyTimeStr")
        @Expose
        var lastReplyTimeStr: String = "",
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
        constructor(lastReplyMessage: String, lastReplyTimeStr: String, contact: ItemChatAttributesContactPojo?)
                :this(lastReplyMessage, lastReplyTimeStr, ChatItemListViewHolder.STATE_CHAT_UNREAD, 1, 0, contact)
}