package com.tokopedia.topchat.chatlist.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder

/**
 * @author : Steven 2019-08-08
 */
data class ItemChatAttributesPojo(
        @SerializedName("label")
        @Expose
        var label: String = "",
        @SerializedName("isReplyByTopbot")
        @Expose
        var isReplyByTopbot: Boolean = false,
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
        @SerializedName("unreadsreply")
        @Expose
        var unreadReply: Int = 0,
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


    // Constructor to create new chat item if it is not on the list
    constructor(
            lastReplyMessage: String,
            lastReplyTimeStr: String,
            contact: ItemChatAttributesContactPojo?
    ) : this(
            lastReplyMessage = lastReplyMessage,
            lastReplyTimeStr = lastReplyTimeStr,
            readStatus = ChatItemListViewHolder.STATE_CHAT_UNREAD,
            unreads = 1,
            unreadReply = 1,
            fraudStatus = 0,
            pinStatus = 0,
            contact = contact
    ) {
        lastReplyTimestamp = lastReplyTimeStr.toLongOrZero()
    }

    var lastReplyTimestamp: Long = 0
}