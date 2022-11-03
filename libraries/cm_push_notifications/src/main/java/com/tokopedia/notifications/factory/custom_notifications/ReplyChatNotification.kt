package com.tokopedia.notifications.factory.custom_notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.factory.RichDefaultNotification
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.user.session.UserSession

class ReplyChatNotification(
    context: Context,
    baseNotificationModel: BaseNotificationModel
) : RichDefaultNotification(context, baseNotificationModel) {

    override fun createNotification(): Notification? {
        val replyAbleNotificationBuilder = setupReplyAbleNotificationBuilder()
        return replyAbleNotificationBuilder.build()
    }

    private fun setupReplyAbleNotificationBuilder(): NotificationCompat.Builder {
        val builder = builder
        setContentNotification(builder)
        setNotificationContentIntent(builder)
        setMediaNotification(builder)
        if (hasActionButton()) {
            setActionButton(builder)
        }
        setNotificationIcon(builder)
        addReplyChatAction(builder)
        return builder
    }

    private fun setContentNotification(builder: NotificationCompat.Builder) {
        builder.setContentTitle(
            CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.title)
        )
        builder.setContentText(
            CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message)
        )
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

    private fun setMediaNotification(builder: NotificationCompat.Builder) {
        val mediaBitmap: Bitmap? = baseNotificationModel.media?.let {
            loadBitmap(baseNotificationModel.media?.mediumQuality)
        }
        if (mediaBitmap != null) {
            setBigPictureNotification(builder, baseNotificationModel, mediaBitmap)
        } else {
            setBigTextStyle(builder)
        }
    }

    private fun addReplyChatAction(builder: NotificationCompat.Builder) {
        try {
            builder.setShowWhen(true)
            builder.addAction(replyAction())
        } catch (ignored: Exception) {
        }
    }

    private fun replyAction(): NotificationCompat.Action {
        return NotificationCompat.Action.Builder(
            android.R.drawable.stat_notify_chat,
            REPLY_LABEL,
            getReplyChatPendingIntent()
        )
            .addRemoteInput(remoteInput())
            .setAllowGeneratedReplies(true)
            .build()
    }

    private fun getReplyChatPendingIntent(): PendingIntent? {
        val notificationId = getTruncatedMessageId(
            baseNotificationModel.payloadExtra?.topchat?.messageId
        )
        val userSession = UserSession(context)
        val intent = Intent(
            baseNotificationModel.payloadExtra?.intentAction ?: INTENT_ACTION_REPLY
        )
        intent.setPackage(context.packageName)
        intent.putExtra(MESSAGE_ID, baseNotificationModel.payloadExtra?.topchat?.messageId)
        intent.putExtra(NOTIFICATION_ID, notificationId)
        intent.putExtra(USER_ID, userSession.userId)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    /**
     * Notification ID & Request Code will be truncated from Message Id
     * Using truncated message id will give unique value of number
     * and it will not overflow
     */
    fun getTruncatedMessageId(messageId: String?): Int {
        return if (messageId == null) {
            EMPTY_MESSAGE_ID
        } else {
            val result: Int = if (messageId.length > LIMIT_NOTIFICATION_ID) {
                val tempId =
                    messageId.substring(messageId.length - LIMIT_NOTIFICATION_ID, messageId.length)
                tempId.toIntOrZero()
            } else {
                messageId.toIntOrZero()
            }
            result
        }
    }

    private fun remoteInput(): RemoteInput {
        return RemoteInput.Builder(REPLY_KEY).setLabel(REPLY_LABEL).build()
    }

    companion object {
        private const val INTENT_ACTION_REPLY = "NotificationChatServiceReceiver.REPLY_CHAT"
        private const val REPLY_KEY = "reply_chat_key"
        private const val REPLY_LABEL = "Reply"
        private const val MESSAGE_ID = "message_chat_id"
        private const val NOTIFICATION_ID = "notification_id"
        private const val USER_ID = "user_id"
        private const val EMPTY_MESSAGE_ID = 0
        private const val LIMIT_NOTIFICATION_ID = 4
    }
}
