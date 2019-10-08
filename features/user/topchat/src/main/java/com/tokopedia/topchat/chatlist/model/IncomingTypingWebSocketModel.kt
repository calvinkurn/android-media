package com.tokopedia.topchat.chatlist.model

import com.tokopedia.topchat.chatlist.pojo.ItemChatAttributesContactPojo


/**
 * @author : Steven 2019-08-09
 */
data class IncomingTypingWebSocketModel(
        val msgId: String = "",
        val isTyping: Boolean = false,
        val contact: ItemChatAttributesContactPojo?
): BaseIncomingItemWebSocketModel(msgId) {

}