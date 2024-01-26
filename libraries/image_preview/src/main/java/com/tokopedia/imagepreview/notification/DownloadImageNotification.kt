package com.tokopedia.imagepreview.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import com.tokopedia.resources.common.R as resourcescommonR

class DownloadImageNotification (
    private val notificationId: Int,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManager
) {

    fun notifyProcessDownload(
        filenameParam: String,
        contentText: String,
        largeIcon: Bitmap
    ) {
        notificationBuilder.setContentTitle(filenameParam)
            .setContentText(contentText)
            .setSmallIcon(resourcescommonR.drawable.ic_status_bar_notif_customerapp)
            .setLargeIcon(largeIcon)
            .setAutoCancel(true)
        notificationBuilder.setProgress(0, 0, true)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    fun notifyNotification(pendingIntent: PendingIntent? = null, contentText: String) {
        notificationBuilder.setContentText(contentText)
            .setProgress(0, 0, false).also {
                if (pendingIntent != null) {
                    it.setContentIntent(pendingIntent)
                }
            }

        notificationBuilder.build().flags =
            notificationBuilder.build().flags or Notification.FLAG_AUTO_CANCEL

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
