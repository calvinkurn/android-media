package com.tokopedia.tokochat.view.mapper

import android.content.Context
import android.text.format.DateFormat
import com.gojek.conversations.database.chats.ConversationsMessage
import com.gojek.conversations.utils.ConversationsConstants
import com.google.gson.GsonBuilder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokochat.di.TokoChatContext
import com.tokopedia.tokochat.domain.response.extension.TokoChatExtensionData
import com.tokopedia.tokochat.domain.response.extension.TokoChatExtensionPayload
import com.tokopedia.tokochat.R
import com.tokopedia.tokochat.util.TokoChatValueUtil.PICTURE
import com.tokopedia.tokochat.util.TokoChatValueUtil.VOICE_NOTES
import com.tokopedia.tokochat_common.view.uimodel.TokoChatHeaderDateUiModel
import com.tokopedia.tokochat_common.view.uimodel.TokoChatImageBubbleUiModel
import com.tokopedia.tokochat_common.view.uimodel.TokoChatMessageBubbleUiModel
import com.tokopedia.tokochat_common.view.uimodel.TokoChatReminderTickerUiModel
import java.util.*
import javax.inject.Inject

class TokoChatConversationUiMapper @Inject constructor(
    @TokoChatContext private val context: Context
) {

    private val gson = GsonBuilder().create()

    private var firstTicker: TokoChatReminderTickerUiModel? = null

    fun setFirstTicker(reminderTickerUiModel: TokoChatReminderTickerUiModel?) {
        firstTicker = reminderTickerUiModel
    }

    fun mapToChatUiModel(
        list: List<ConversationsMessage>,
        userId: String
    ): List<Any> {
        val resultList = arrayListOf<Any>()
        var lastHeaderDate: TokoChatHeaderDateUiModel? = null
        list.forEach {
            when (it.customType) {
                ConversationsConstants.ADMIN_MESSAGE -> {
                    resultList.add(it.mapToTickerUiModel())
                }
                ConversationsConstants.EXTENSION_MESSAGE -> {
                    val extensionData = convertToExtensionData(it.messageData)
                    when (extensionData?.messageId) {
                        PICTURE -> {
                            resultList.add(it.mapToImageUiModel(extensionData, userId))
                        }
                        VOICE_NOTES -> {
                            resultList.add(it.mapToMessageBubbleUiModel(
                                userId = userId,
                                isNotSupported = true,
                                context.getString(R.string.tokochat_unsupported_attachment_voice_note)
                            ))
                        }
                        else -> {
                            resultList.add(it.mapToMessageBubbleUiModel(
                                userId = userId,
                                isNotSupported = true,
                                context.getString(R.string.tokochat_unsupported_attachment_general)
                            ))
                        }
                    }
                }
                ConversationsConstants.TEXT_MESSAGE -> {
                    resultList.add(it.mapToMessageBubbleUiModel(userId))
                }
            }

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

        firstTicker?.let {
            resultList.add(resultList.size, it)
        }
        return resultList
    }

    private fun ConversationsMessage.mapToMessageBubbleUiModel(
        userId: String,
        isNotSupported: Boolean = false,
        unsupportedMessageText: String = ""
    ): TokoChatMessageBubbleUiModel {
        val messageText = if (isNotSupported) {
            unsupportedMessageText
        } else {
            this.messageText
        }
        return TokoChatMessageBubbleUiModel.Builder()
            .withMessageId(this.messageId)
            .withFromUserId(this.messageSender?.userId ?: "")
            .withMessageTime(this.createdTimestamp)
            .withMessageStatus(this.readReceipt)
            .withIsSender(this.messageSender?.userId == userId)
            .withMessageText(messageText)
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

    private fun sameDay(messageTime: Long, previousMessageTime: Long): Boolean {
        return compareTime(messageTime, previousMessageTime)
    }

    private fun compareTime(calCurrent: Long, calBefore: Long): Boolean {
        return DateFormat.getLongDateFormat(context)
            .format(Date(calCurrent)) == DateFormat.getLongDateFormat(context).format(
            Date(calBefore)
        )
    }

    private fun ConversationsMessage.mapToTickerUiModel(): TokoChatReminderTickerUiModel {
        return TokoChatReminderTickerUiModel(
            message = this.messageText,
            tickerType = Int.ZERO
        )
    }

    private fun convertToExtensionData(data: String?): TokoChatExtensionData? {
        return try {
            if (data.isNullOrEmpty()) {
                null
            } else {
                val result = gson.fromJson(data, TokoChatExtensionData::class.java)
                result.extensionPayload = gson.fromJson(result.payload, TokoChatExtensionPayload::class.java)
                result
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            null
        }
    }

    private fun ConversationsMessage.mapToImageUiModel(
        data: TokoChatExtensionData,
        userId: String
    ): TokoChatImageBubbleUiModel {
        return TokoChatImageBubbleUiModel.Builder()
            .withImageUrl(data.extensionPayload?.id?: "")
            .withMessageId(this.messageId)
            .withFromUserId(this.messageSender?.userId ?: "")
            .withMessageTime(this.createdTimestamp)
            .withMessageStatus(this.readReceipt)
            .withIsSender(this.messageSender?.userId == userId)
            .build()
    }
}
