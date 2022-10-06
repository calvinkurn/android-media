package com.tokopedia.tokochat.view.mapper

import android.content.Context
import android.text.format.DateFormat
import com.gojek.conversations.database.chats.ConversationsMessage
import com.gojek.conversations.utils.ConversationsConstants
import com.tokopedia.tokochat.di.TokoChatContext
import com.tokopedia.tokochat_common.view.uimodel.TokoChatHeaderDateUiModel
import com.tokopedia.tokochat_common.view.uimodel.TokoChatMessageBubbleBaseUiModel
import java.util.*
import javax.inject.Inject

class TokoChatConversationUiMapper @Inject constructor(
    @TokoChatContext private val context: Context
) {

    private var bottomMostHeaderDate: TokoChatHeaderDateUiModel? = null
    private var topMostHeaderDate: TokoChatHeaderDateUiModel? = null

    fun mapToChatUiModel(
        list: List<ConversationsMessage>,
        userId: String
    ): List<Any> {
        val resultList = arrayListOf<Any>()
        var lastHeaderDate: TokoChatHeaderDateUiModel? = null
        list.forEach {
            when (it.customType) {
                ConversationsConstants.ADMIN_MESSAGE -> {
                    //todo: put ticker ui model
                }
                ConversationsConstants.EXTENSION_MESSAGE -> {
                    //todo: put image ui model
                }
                ConversationsConstants.TEXT_MESSAGE -> {
                    resultList.add(it.mapToMessageBubbleUiModel(userId))

                    // Get current header from time
                    val headerUiModel = it.mapToHeaderDateUiModel()
                    // If the last recorded header is null, put the value
                    if (lastHeaderDate == null) {
                        lastHeaderDate = headerUiModel
                    }

                    /** If the header date is different
                     * ex: last -> 4 Oct, Current -> 6 Oct
                     * Then render the header and replace the last header
                     */
                    if (it.shouldShowHeaderDate(lastHeaderDate, it == list.last())) {
                        lastHeaderDate?.let { date ->
                            resultList.add(date)
                        }
                        lastHeaderDate = headerUiModel
                    }
                }
            }
        }
        return resultList
    }

    private fun ConversationsMessage.mapToMessageBubbleUiModel(
        userId: String
    ): TokoChatMessageBubbleBaseUiModel {
        val isNotSupported = this.customType == ConversationsConstants.EXTENSION_MESSAGE
        return TokoChatMessageBubbleBaseUiModel.Builder()
            .withMessageId(this.messageId)
            .withFromUserId(this.messageSender?.userId ?: "")
            .withMessageTime(this.createdTimestamp)
            .withMessageStatus(this.readReceipt)
            .withIsSender(this.messageSender?.userId == userId)
            .withMessageText(this.messageText)
            .withIsNotSupported(isNotSupported)
            .build()
    }

    private fun ConversationsMessage.mapToHeaderDateUiModel(): TokoChatHeaderDateUiModel {
        return TokoChatHeaderDateUiModel(
            this.createdDate, this.createdTimestamp
        )
    }

    private fun ConversationsMessage.shouldShowHeaderDate(
        lastHeaderDate: TokoChatHeaderDateUiModel?,
        isLastItem: Boolean
    ): Boolean {
       return (
           !sameDay(this.createdTimestamp, lastHeaderDate?.dateTimestamp?: 0)
               || isLastItem
       )
    }

    //TODO: Add/Remove for load more date

    private fun sameDay(messageTime: Long, previousMessageTime: Long): Boolean {
        return compareTime(messageTime, previousMessageTime)
    }

    private fun compareTime(calCurrent: Long, calBefore: Long): Boolean {
        return DateFormat.getLongDateFormat(context)
            .format(Date(calCurrent)) == DateFormat.getLongDateFormat(context).format(
            Date(calBefore)
        )
    }
}
