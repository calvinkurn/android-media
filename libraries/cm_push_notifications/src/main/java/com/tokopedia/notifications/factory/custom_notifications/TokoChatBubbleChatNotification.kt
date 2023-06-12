package com.tokopedia.notifications.factory.custom_notifications

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.bubbles.data.model.BubbleHistoryItemModel
import com.tokopedia.bubbles.data.model.BubbleNotificationModel
import com.tokopedia.bubbles.factory.BubblesFactoryImpl
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.factory.RichDefaultNotification
import com.tokopedia.notifications.model.BaseNotificationModel
import timber.log.Timber
import kotlin.math.max

class TokoChatBubbleChatNotification(
    context: Context,
    baseNotificationModel: BaseNotificationModel,
    private val baseNotificationList: List<BaseNotificationModel>,
    private val bubbleBitmap: Bitmap?
) : RichDefaultNotification(context, baseNotificationModel, baseNotificationList) {

    private val bubblesFactory = BubblesFactoryImpl(context)

    override fun createNotification(): Notification {
        val bubbleAbleNotificationBuilder = setupBubbleAbleNotificationBuilder()
        return bubbleAbleNotificationBuilder.build()
    }

    private fun setupBubbleAbleNotificationBuilder(): NotificationCompat.Builder {
        val builder = builder
        replaceNotificationId(getMessageId(baseNotificationModel.appLink.orEmpty()))
        setContentNotification(builder)
        setNotificationContentIntent(builder)
        baseNotificationModel.appLink?.let {
            builder.setGroup(getMessageId(it))
        }
        if (hasActionButton()) {
            setActionButton(builder)
        }
        setNotificationIcon(builder)
        setupBubble(builder, baseNotificationModel)
        return builder
    }

    private fun replaceNotificationId(messageId: String) {
        val orderIdNumber = Regex(REGEX_ORDER_ID).replace(messageId, String.EMPTY)
        baseNotificationModel.notificationId = orderIdNumber
            .substring(max(orderIdNumber.length - NOTIFICATION_ID_LENGTH, Int.ZERO))
            .toIntOrZero()
    }

    private fun setContentNotification(builder: NotificationCompat.Builder) {
        builder.setContentTitle(
            CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.title)
        )
        builder.setContentText(
            CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message)
        )
        setBigTextStyle(builder)
        builder.setSmallIcon(drawableIcon)
        builder.setAutoCancel(true)
    }

    private fun setNotificationContentIntent(builder: NotificationCompat.Builder) {
        builder.setContentIntent(createMainPendingIntent(baseNotificationModel, requestCode))
        builder.setDeleteIntent(
            createDismissPendingIntent(
                baseNotificationModel.notificationId,
                requestCode
            )
        )
    }

    private fun setupBubble(
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
        val shortcutId = getMessageId(baseNotificationModel.appLink.orEmpty())
        val notificationType = Int.ZERO
        val senderId = ""
        val fullName = baseNotificationModel.title.orEmpty()
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
            when (uri.host) {
                TOKOCHAT_HOST -> {
                    uri.getQueryParameter(ApplinkConst.TokoChat.ORDER_ID_GOJEK).orEmpty()
                } else -> {
                    uri.lastPathSegment.orEmpty()
                }
            }
        } catch (e: NullPointerException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            "0"
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
        val senderName = baseNotificationModel.title.orEmpty()
        val avatarUrl = baseNotificationModel.icon.orEmpty()
        val shortcutId = getMessageId(baseNotificationModel.appLink.orEmpty())
        return BubbleHistoryItemModel(
            shortcutId,
            appLink,
            senderName,
            avatarUrl
        )
    }

    private fun getBubbleHistorySingleItems(baseNotificationModel: BaseNotificationModel): List<BubbleHistoryItemModel> {
        val appLink = baseNotificationModel.appLink.orEmpty()
        val senderName = baseNotificationModel.title.orEmpty()
        val avatarUrl = baseNotificationModel.icon.orEmpty()
        val shortcutId = getMessageId(baseNotificationModel.appLink.orEmpty())
        val historyItemModel = BubbleHistoryItemModel(
            shortcutId,
            appLink,
            senderName,
            avatarUrl
        )
        return listOf(historyItemModel)
    }

    companion object {
        const val TOKOCHAT_HOST = "tokochat"
        private const val NOTIFICATION_ID_LENGTH = 9
        private const val REGEX_ORDER_ID = "\\D+"
    }
}
