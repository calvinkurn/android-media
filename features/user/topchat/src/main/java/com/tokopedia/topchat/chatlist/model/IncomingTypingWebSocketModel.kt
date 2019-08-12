package com.tokopedia.topchat.chatlist.model


/**
 * @author : Steven 2019-08-09
 */
data class IncomingTypingWebSocketModel(val msgId: String = "", val isTyping: Boolean = false): BaseIncomingItemWebSocketModel(msgId) {

}