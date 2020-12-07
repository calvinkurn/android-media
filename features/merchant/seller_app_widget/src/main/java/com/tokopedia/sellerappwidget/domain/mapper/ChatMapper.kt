package com.tokopedia.sellerappwidget.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerappwidget.common.Utils
import com.tokopedia.sellerappwidget.data.model.ChatListModel
import com.tokopedia.sellerappwidget.di.AppWidgetScope
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 01/12/20
 */

@AppWidgetScope
class ChatMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(chatList: List<ChatListModel>): List<ChatUiModel> {
        return chatList.map {
            ChatUiModel(
                    messageId = it.msgID.orZero(),
                    messageKey = it.messageKey.orEmpty(),
                    userDisplayName = it.attributes?.contact?.name.orEmpty(),
                    lastMessage = it.attributes?.lastReplyMessage.orEmpty(),
                    lastReplyTime = getLastReplyTimeStr(it.attributes?.lastReplyTimeStr.orEmpty())
            )
        }
    }

    private fun getLastReplyTimeStr(lastReplyTime: String): String {
        if (lastReplyTime.isBlank()) return lastReplyTime

        return try {
            val lastReplyTimeMillis = lastReplyTime.toLong()
            val lastReplyTimeDate = Date(lastReplyTimeMillis)
            val todayMillis = Date().time
            val oneDayMillis = TimeUnit.DAYS.toMillis(1)
            val twoDaysMillis = TimeUnit.DAYS.toMillis(2)
            val diffMillis = lastReplyTimeMillis.minus(todayMillis)
            return when {
                diffMillis < oneDayMillis -> { //ex : 13:00
                    val format = "HH:mm"
                    Utils.formatDate(lastReplyTimeDate, format)
                }
                diffMillis in oneDayMillis until twoDaysMillis -> { //ex : Kemarin
                    val yesterday = "Kemarin"
                    yesterday
                }
                else -> { //ex : 08 Nov
                    val format = "dd MMM"
                    Utils.formatDate(lastReplyTimeDate, format)
                }
            }
        } catch (e: NumberFormatException) {
            lastReplyTime
        }
    }
}