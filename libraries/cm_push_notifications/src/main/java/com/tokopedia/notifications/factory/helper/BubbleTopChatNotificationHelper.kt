package com.tokopedia.notifications.factory.helper

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.tokopedia.bubbles.data.model.BubbleHistoryItemModel
import com.tokopedia.bubbles.data.model.BubbleNotificationModel
import com.tokopedia.bubbles.factory.BubblesFactory
import com.tokopedia.notifications.model.BaseNotificationModel
import timber.log.Timber

class BubbleTopChatNotificationHelper(
    private val baseNotificationList: List<BaseNotificationModel>,
    private val bubblesFactory: BubblesFactory,
    private val bubbleBitmap: Bitmap?
) {

    fun setupBubble(
        builder: NotificationCompat.Builder,
        baseNotificationModel: BaseNotificationModel
    ) {
        try {
            val bubbleNotificationModel = getBubbleNotificationModel(
                baseNotificationModel
            )
            updateBubblesShortcuts(
                bubbleNotificationModel,
                baseNotificationModel
            )
            updateBubblesBuilder(builder, bubbleNotificationModel)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun updateBubblesBuilder(
        builder: NotificationCompat.Builder,
        bubbleNotificationModel: BubbleNotificationModel
    ) {
        bubblesFactory.setupBubble(builder, bubbleNotificationModel, bubbleBitmap)
    }

    private fun updateBubblesShortcuts(
        bubbleNotificationModel: BubbleNotificationModel,
        baseNotificationModel: BaseNotificationModel
    ) {
        val historyItemModels: List<BubbleHistoryItemModel> = if (bubbleNotificationModel.isFromUser) {
            getBubbleHistorySingleItems(baseNotificationModel)
        } else {
            getBubbleHistoryItems(baseNotificationList, baseNotificationModel)
        }
        bubblesFactory.updateShorcuts(historyItemModels, bubbleNotificationModel, bubbleBitmap)
    }

    private fun getBubbleNotificationModel(
        baseNotificationModel: BaseNotificationModel
    ): BubbleNotificationModel {
        val appLink = baseNotificationModel.appLink.orEmpty()
        val shortcutId = getMessageId(appLink)
        val notificationType = NOTIFICATION_CHAT_TYPE
        val senderId = "" //TODO need to confirm to pak alih, what is the payload of response
        val fullName = baseNotificationModel.title.orEmpty() //TODO need to confirm to pak alih, what is the payload of response
        val avatarUrl = baseNotificationModel.icon.orEmpty()
        val summary = baseNotificationModel.message.orEmpty()
        val sentTime = System.currentTimeMillis()

        return BubbleNotificationModel(
            notificationType = notificationType,
            notificationId = baseNotificationModel.notificationId,
            shortcutId = shortcutId,
            senderId = senderId,
            applinks = appLink,
            fullName = fullName,
            avatarUrl = avatarUrl,
            summary = summary,
            sentTime = sentTime,
            isFromUser = false
        )
    }

    private fun getMessageId(appLinks: String): String {
        return try {
            val uri = Uri.parse(appLinks)
            uri.lastPathSegment ?: MESSAGE_ID_ZERO
        } catch (e: NullPointerException) {
            MESSAGE_ID_ZERO
        }
    }

    private fun getBubbleHistoryItems(historyNotificationList: List<BaseNotificationModel>, baseNotificationModel: BaseNotificationModel): List<BubbleHistoryItemModel> {
        val mappedResult = mutableListOf<BubbleHistoryItemModel>()
        if (historyNotificationList.isNotEmpty()) {
            for (item in historyNotificationList) {
                mappedResult.add(mapToBubbleHistoryItemModel(item))
            }
        } else {
            mappedResult.add(mapToBubbleHistoryItemModel(baseNotificationModel))
        }
        return mappedResult.toList()
    }

    private fun mapToBubbleHistoryItemModel(baseNotificationModel: BaseNotificationModel): BubbleHistoryItemModel {
        val appLink = baseNotificationModel.appLink.orEmpty()
        val senderName = baseNotificationModel.title.orEmpty() //TODO need to confirm to pak alih, what is the payload of response
        val avatarUrl = baseNotificationModel.icon.orEmpty()
        val shortcutId = getMessageId(appLink)
        return BubbleHistoryItemModel(
            shortcutId,
            appLink,
            senderName,
            avatarUrl
        )
    }

    private fun getBubbleHistorySingleItems(baseNotificationModel: BaseNotificationModel): List<BubbleHistoryItemModel> {
        val appLink = baseNotificationModel.appLink.orEmpty()
        val senderName = baseNotificationModel.title.orEmpty() //TODO need to confirm to pak alih, what is the payload of response
        val avatarUrl = baseNotificationModel.icon.orEmpty()
        val shortcutId = getMessageId(appLink)
        val historyItemModel = BubbleHistoryItemModel(
            shortcutId,
            appLink,
            senderName,
            avatarUrl
        )
        return listOf(historyItemModel)
    }

    companion object {
        private const val MESSAGE_ID_ZERO = "0"
        private const val NOTIFICATION_CHAT_TYPE = 300
    }
}
