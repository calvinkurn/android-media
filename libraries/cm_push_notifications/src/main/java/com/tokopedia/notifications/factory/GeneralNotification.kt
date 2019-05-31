package com.tokopedia.notifications.factory

import android.app.Notification
import android.content.Context
import android.support.v4.app.NotificationCompat

import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.model.BaseNotificationModel

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
class GeneralNotification internal constructor(context: Context, baseNotificationModel: BaseNotificationModel) : BaseNotification(context, baseNotificationModel) {

    override fun createNotification(): Notification {
        val builder = builder
        builder.setContentTitle(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.title))
        builder.setContentText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message))
        builder.setSmallIcon(drawableIcon)
        builder.setContentIntent(createMainPendingIntent(baseNotificationModel, requestCode))
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.notificationId, requestCode))
        builder.setAutoCancel(true)

        baseNotificationModel.detailMessage?.apply {
            if (!this.isBlank()) {
                builder.setStyle(NotificationCompat.BigTextStyle()
                        .bigText(this))
            }
        }
        return builder.build()
    }


}
