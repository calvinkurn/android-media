package com.tokopedia.chatbot.util

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object ChatbotNewRelicLogger {

    fun logNewRelic(
        key: String,
        isSuccess: Boolean,
        messageId: String,
        exception: Throwable? = null
    ) {
        val map: MutableMap<String, String> = HashMap()
        var message = exception?.message
        var messageContent = (message?.subSequence(
            0,
            Math.min(message.length, MAX_LENGTH_FOR_NR_EXCEPTION)
        )).toString()


        map["type"] = "request"
        map["success"] = if (isSuccess) "true" else "false"
        map["messageId"] = messageId
        map["exception"] = messageContent
        ServerLogger.log(Priority.P2, key, map)
    }

    fun logNewRelicForGetChatReplies(
        key: String,
        isSuccess: Boolean,
        messageId: Long,
        exception: Exception,
        beforeReplyTime: String,
        afterReplyTime: String
    ) {
        val map: MutableMap<String, String> = HashMap()
        var message = exception.message
        var messageContent = (message?.subSequence(
            0,
            Math.min(message.length, MAX_LENGTH_FOR_NR_EXCEPTION)
        )).toString()

        map["type"] = "request"
        map["success"] = if (isSuccess) "true" else "false"
        map["messageId"] = messageId.toString()
        map["exception"] = messageContent
        map["beforeReplyTime"] = beforeReplyTime
        map["afterReplyTime"] = afterReplyTime
        ServerLogger.log(Priority.P2, key, map)
    }

    fun logNewRelicForSocket(
        exception: Exception
    ) {
        val map: MutableMap<String, String> = HashMap()

        var message = exception.message
        var messageContent = (message?.subSequence(
            0,
            Math.min(message.length, MAX_LENGTH_FOR_NR_EXCEPTION)
        )).toString()

        map["type"] = "request"
        map["success"] = "false"
        map["exception"] = messageContent
        ServerLogger.log(Priority.P2, "CHATBOT_SOCKET_EXCEPTION", map)
    }


    private const val MAX_LENGTH_FOR_NR_EXCEPTION = 300

}