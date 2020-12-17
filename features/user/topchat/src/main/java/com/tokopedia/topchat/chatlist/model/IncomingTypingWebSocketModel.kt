package com.tokopedia.topchat.chatlist.model

import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.topchat.chatlist.pojo.ItemChatAttributesContactPojo


/**
 * @author : Steven 2019-08-09
 */
data class IncomingTypingWebSocketModel constructor(
        val msgId: String = "",
        val isTyping: Boolean = false,
        val contact: ItemChatAttributesContactPojo?
): BaseIncomingItemWebSocketModel(msgId) {

    override fun getContactId(): String {
        return contact?.contactId.toEmptyStringIfNull()
    }

    override fun getTag(): String {
        return contact?.tag.toEmptyStringIfNull()
    }

}