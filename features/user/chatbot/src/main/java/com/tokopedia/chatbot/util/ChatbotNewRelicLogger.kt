package com.tokopedia.chatbot.util

import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object ChatbotNewRelicLogger {

    fun logNewRelic(
        isSuccess: Boolean,
        messageId: String,
        gqlKey: String,
        exception: Throwable? = null,
        key: String = ChatbotConstant.NewRelic.KEY_CHATBOT_ERROR
    ) {
        val map: MutableMap<String, String> = HashMap()
        val message = exception?.message
        val messageContent = (
            message?.subSequence(
                0,
                Math.min(message.length, MAX_LENGTH_FOR_NR_EXCEPTION)
            )
            ).toString()

        map["type"] = "request"
        map["success"] = if (isSuccess) "true" else "false"
        map["messageId"] = messageId
        map["gql_name"] = gqlKey
        map["exception"] = messageContent
        ServerLogger.log(Priority.P2, key, map)
    }

    fun logNewRelicForGetChatReplies(
        isSuccess: Boolean,
        messageId: String,
        gqlKey: String,
        exception: Throwable? = null,
        beforeReplyTime: String,
        afterReplyTime: String,
        key: String = ChatbotConstant.NewRelic.KEY_CHATBOT_ERROR
    ) {
        val map: MutableMap<String, String> = HashMap()
        val message = exception?.message
        val messageContent = (
            message?.subSequence(
                0,
                Math.min(message.length, MAX_LENGTH_FOR_NR_EXCEPTION)
            )
            ).toString()

        map["type"] = "request"
        map["success"] = if (isSuccess) "true" else "false"
        map["messageId"] = messageId
        map["gql_name"] = gqlKey
        map["exception"] = messageContent
        map["beforeReplyTime"] = beforeReplyTime
        map["afterReplyTime"] = afterReplyTime
        ServerLogger.log(Priority.P2, key, map)
    }

    fun logNewRelicForSocket(
        exception: Throwable? = null
    ) {
        val map: MutableMap<String, String> = HashMap()

        val message = exception?.message ?: ""
        val messageContent = (
            message.subSequence(
                0,
                Math.min(message.length, MAX_LENGTH_FOR_NR_EXCEPTION)
            )
            ).toString()

        map["type"] = "request"
        map["success"] = "false"
        map["exception"] = messageContent
        map["socket_error"] = "true"
        ServerLogger.log(Priority.P2, ChatbotConstant.NewRelic.KEY_CHATBOT_ERROR, map)
    }

    // This logger is used to send data related to attachment 22 and 23
    fun logNewRelicForCSAT(messageId: String, attachmentType: String, caseId: String, caseChatId: String) {
        val map: MutableMap<String, String> = HashMap()
        map["type"] = "request"
        map["messageId"] = messageId
        map["attachmentType"] = attachmentType
        map["caseId"] = caseId
        map["caseChatId"] = caseChatId
        ServerLogger.log(Priority.P2, ChatbotConstant.NewRelic.KEY_CSAT, map)
    }

    private const val MAX_LENGTH_FOR_NR_EXCEPTION = 300
}
