package com.tokopedia.notifications.factory

import android.app.Notification
import android.content.Context
import android.text.TextUtils
import androidx.core.app.NotificationCompat

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

        if (!TextUtils.isEmpty(baseNotificationModel.detailMessage))
            builder.setStyle(NotificationCompat.BigTextStyle()
                    .bigText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.detailMessage)))
        else if (!TextUtils.isEmpty(baseNotificationModel.message))
            builder.setStyle(NotificationCompat.BigTextStyle()
                    .bigText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message)))
        return builder.build()
    }


}
