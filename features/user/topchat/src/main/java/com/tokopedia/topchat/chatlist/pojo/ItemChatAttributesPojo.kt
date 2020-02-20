package com.tokopedia.topchat.chatlist.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder

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
        var readStatus: Int = ChatItemListViewHolder.STATE_CHAT_UNREAD,
        @SerializedName("unreads")
        @Expose
        var unreads: Int = 1,
        @SerializedName("fraudStatus")
        @Expose
        var fraudStatus: Int = 0,
        @SerializedName("pinStatus")
        @Expose
        var pinStatus: Int = 0,
        @SerializedName("contact")
        @Expose
        var contact: ItemChatAttributesContactPojo?

) {
    constructor(
            lastReplyMessage: String,
            lastReplyTimeStr: String,
            contact: ItemChatAttributesContactPojo?
    ) : this(
            lastReplyMessage,
            lastReplyTimeStr,
            ChatItemListViewHolder.STATE_CHAT_UNREAD,
            1,
            0,
            0,
            contact)
}