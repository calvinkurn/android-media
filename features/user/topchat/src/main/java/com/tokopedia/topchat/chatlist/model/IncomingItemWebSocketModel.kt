package com.tokopedia.topchat.chatlist.model

import com.tokopedia.topchat.chatlist.pojo.ItemChatAttributesContactPojo


/**
 * @author : Steven 2019-08-09
 */
data class IncomingItemWebSocketModel(val messageId: String = "") {

    var message: String = ""
    var unreadCounter: Int = 0
    var time: String = ""
    var contact: ItemChatAttributesContactPojo? = null



    constructor(messageId: String,
                message: String,
                time: String,
                contact: ItemChatAttributesContactPojo): this(messageId) {
        this.message = message
        this.time = time
        this.contact = contact
    }
}