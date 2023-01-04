package com.tokopedia.chatbot.util

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority


private const val CHATBOT_MESSAGE_ID = "CHATBOT_MESSAGE_ID_ERROR"

fun String?.convertMessageIdToLong(): Long {
    val convertedMessageId = this.toLongOrZero()
    if (convertedMessageId == 0L) {
        logRequest(this)
    }
    return convertedMessageId
}
private fun logRequest(messageId : String?) {
    val map: MutableMap<String, String> = HashMap()
    map["type"] = "request"
    map["messageId"] = messageId ?: ""
    map["reason"] = "Invalid MessageId"
    ServerLogger.log(Priority.P2, CHATBOT_MESSAGE_ID, map)
}
