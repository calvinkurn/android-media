package com.tokopedia.chatbot.util

import com.tokopedia.chatbot.ChatbotConstant.NewRelic.CHATBOT_MESSAGE_ID
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

fun String?.convertMessageIdToLong(): Long {
    val convertedMessageId = this.toLongOrZero()
    if (convertedMessageId == 0L) {
        logNewRelicMessageIdError(this, "")
    }
    return convertedMessageId
}
fun logNewRelicMessageIdError(messageId: String?, pageSource: String) {
    val map: MutableMap<String, String> = HashMap()
    map["type"] = "request"
    map["messageId"] = messageId ?: ""
    map["reason"] = "Invalid MessageId"
    map["page_source"] = pageSource
    ServerLogger.log(Priority.P2, CHATBOT_MESSAGE_ID, map)
}
