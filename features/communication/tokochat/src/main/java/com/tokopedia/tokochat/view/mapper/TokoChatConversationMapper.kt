package com.tokopedia.tokochat.view.mapper

import com.gojek.conversations.babble.message.data.MessageType
import com.gojek.conversations.database.chats.ConversationsMessage
import com.tokopedia.tokochat_common.view.uimodel.TokoChatMessageBubbleBaseUiModel

object TokoChatConversationMapper {

    private const val SYSTEM_MESSAGE = "Dikirim oleh sistem"

    fun List<ConversationsMessage>.mapToMessageBubbleUi(
        userId: String
    ): List<TokoChatMessageBubbleBaseUiModel> {
        val resultList = arrayListOf<TokoChatMessageBubbleBaseUiModel>()
        forEach {
            val label = if (it.customType == MessageType.System.type) {
                SYSTEM_MESSAGE
            } else {
                ""
            }
            val isNotSupported = it.customType == MessageType.Extension.type
            val bubbleMessage = TokoChatMessageBubbleBaseUiModel.Builder()
                .withMessageId(it.messageId)
                .withFromUserId(it.messageSender?.userId?: "")
                .withMessageTime(it.createdTimestamp)
                .withMessageStatus(it.readReceipt)
                .withIsSender(it.messageSender?.userId == userId)
                .withMessageText(it.messageText)
                .withLabel(label)
                .withIsNotSupported(isNotSupported)
                .build()
            resultList.add(bubbleMessage)
        }
        return resultList
    }


}
