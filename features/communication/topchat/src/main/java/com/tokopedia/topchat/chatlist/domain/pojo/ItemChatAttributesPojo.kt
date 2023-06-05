package com.tokopedia.topchat.chatlist.domain.pojo

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.chatlist.view.adapter.viewholder.ChatItemListViewHolder

/**
 * @author : Steven 2019-08-08
 */
data class ItemChatAttributesPojo(
    @SerializedName("label")
    var label: String = "",
    @SerializedName("isReplyByTopbot")
    var isReplyByTopbot: Boolean = false,
    @SerializedName("lastReplyMessage")
    var lastReplyMessage: String = "",
    @SerializedName("lastReplyTimeStr")
    var lastReplyTimeStr: String = "",
    @SerializedName("readStatus")
    var readStatus: Int = ChatItemListViewHolder.STATE_CHAT_UNREAD,
    @SerializedName("unreads")
    var unreads: Int = 1,
    @SerializedName("unreadsreply")
    var unreadReply: Int = 0,
    @SerializedName("fraudStatus")
    var fraudStatus: Int = 0,
    @SerializedName("pinStatus")
    var pinStatus: Int = 0,
    @SerializedName("contact")
    var contact: ItemChatAttributesContactPojo? = null,
    @SerializedName("labelIconURL")
    var labelIcon: String = ""

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
