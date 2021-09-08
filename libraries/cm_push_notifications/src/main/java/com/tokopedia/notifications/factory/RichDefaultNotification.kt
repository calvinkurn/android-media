package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.model.ActionButton
import com.tokopedia.notifications.model.BaseNotificationModel

class RichDefaultNotification internal constructor(
    context: Context,
    baseNotificationModel: BaseNotificationModel
) : BaseNotification(context, baseNotificationModel) {


    override fun createNotification(): Notification? {
        val builder = builder
        builder.setContentTitle(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.title))
        builder.setContentText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message))
        builder.setSmallIcon(drawableIcon)
        builder.setContentIntent(createMainPendingIntent(baseNotificationModel, requestCode))
        builder.setAutoCancel(true)
        builder.setDeleteIntent(
            createDismissPendingIntent(
                baseNotificationModel.notificationId,
                requestCode
            )
        )
        if (baseNotificationModel.media != null)
            setBigPictureNotification(builder, baseNotificationModel)
        else
            setBigTextStyle(builder)
        if (hasActionButton())
            setActionButton(builder)
        return builder.build()
    }

    private fun setBigTextStyle(builder: NotificationCompat.Builder) {
        if (!TextUtils.isEmpty(baseNotificationModel.detailMessage))
            builder.setStyle(NotificationCompat.BigTextStyle()
                .bigText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.detailMessage)))
        else if (!TextUtils.isEmpty(baseNotificationModel.message))
            builder.setStyle(NotificationCompat.BigTextStyle()
                .bigText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message)))
    }

    private fun setActionButton(builder: NotificationCompat.Builder) {
        baseNotificationModel.actionButton.forEach {
            val action = NotificationCompat.Action.Builder(
                0, it.text, getButtonPendingIntent(it)
            ).build()
            builder.addAction(action)
        }
    }

    private fun getButtonPendingIntent(actionButton: ActionButton): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_BUTTON
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_EXTRA, actionButton)
        return getPendingIntent(context, intent, requestCode)
    }

    private fun hasActionButton(): Boolean {
        return baseNotificationModel.actionButton.isNotEmpty()
    }

    private fun setBigPictureNotification(
        builder: NotificationCompat.Builder,
        baseNotificationModel: BaseNotificationModel
    ) {
        val bitmap = loadBitmap(baseNotificationModel.media?.mediumQuality)
        bitmap?.let {
            builder.setLargeIcon(bitmap)
            val bigPictureStyle = NotificationCompat.BigPictureStyle()
                .setSummaryText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.detailMessage))
                .bigLargeIcon(bitmap)
                .bigPicture(bitmap)
            if (!TextUtils.isEmpty(baseNotificationModel.message)) {
                bigPictureStyle.setSummaryText(
                    CMNotificationUtils
                        .getSpannedTextFromStr(baseNotificationModel.message)
                )
            }
            builder.setStyle(bigPictureStyle)
        }
    }

}