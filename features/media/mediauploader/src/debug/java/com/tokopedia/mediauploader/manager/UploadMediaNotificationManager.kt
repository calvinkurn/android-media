package com.tokopedia.mediauploader.manager

import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import java.util.*
import javax.inject.Inject

class UploadMediaNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val notificationManager = context.getSystemService(
        Context.NOTIFICATION_SERVICE
    ) as NotificationManager

    private var notificationId: Int = Random().nextInt()

    private val statusBarMAIcon = com.tokopedia.resources.common.R.drawable.ic_status_bar_notif_customerapp
    private val mainAppMAIcon = com.tokopedia.resources.common.R.drawable.ic_big_notif_customerapp

    private val notificationBuilder = NotificationCompat
        .Builder(context, CHANNEL_GENERAL)
        .apply {
            setSmallIcon(statusBarMAIcon)
            setLargeIcon(BitmapFactory.decodeResource(context.resources, mainAppMAIcon))
            setGroup(NOTIFICATION_GROUP)
            setOnlyAlertOnce(true)
            priority = NotificationCompat.PRIORITY_MAX
        }

    fun onStart() {
        val notification = notificationBuilder
            .setContentTitle(TITLE_SUBMITTING_ATTACHMENT)
            .setContentText(MESSAGE_SUBMITTING_ATTACHMENT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(MESSAGE_SUBMITTING_ATTACHMENT))
            .setProgress(0, 0, true)
            .setOngoing(false)
            .setShowWhen(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun onProgress(progress: Int) {
        val notification = notificationBuilder
            .setContentTitle(TITLE_SUBMITTING_ATTACHMENT)
            .setContentText(MESSAGE_SUBMITTING_ATTACHMENT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(MESSAGE_SUBMITTING_ATTACHMENT))
            .setProgress(100, progress, false)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun onSuccess() {
        val notification = notificationBuilder
            .setContentTitle(TITLE_ATTACHMENT_SUBMITTED)
            .setContentText(MESSAGE_ATTACHMENT_SUBMITTED)
            .setStyle(NotificationCompat.BigTextStyle().bigText(MESSAGE_ATTACHMENT_SUBMITTED))
            .setProgress(100, 100, false)
            .setOngoing(false)
            .setShowWhen(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun onError() {
        val notification = notificationBuilder
            .setContentTitle(TITLE_ATTACHMENT_ERROR)
            .setContentText(MESSAGE_ATTACHMENT_ERROR)
            .setStyle(NotificationCompat.BigTextStyle().bigText(MESSAGE_ATTACHMENT_ERROR))
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun setId(id: Int) {
        notificationId = id
    }

    companion object {
        private const val CHANNEL_GENERAL = "ANDROID_GENERAL_CHANNEL"
        private const val NOTIFICATION_GROUP = "com.tokopedia"
        private const val TITLE_SUBMITTING_ATTACHMENT = "Mengirim Lampiran"
        private const val TITLE_ATTACHMENT_SUBMITTED = "Lampiran selesai dikirim \uD83D\uDCAB"
        private const val TITLE_ATTACHMENT_ERROR = "Lampiran gagal dikirim :pepesad:"
        private const val MESSAGE_SUBMITTING_ATTACHMENT = "Mengirim lampiran"
        private const val MESSAGE_ATTACHMENT_SUBMITTED = "Lampiran terkirim"
        private const val MESSAGE_ATTACHMENT_ERROR = "Lampiran gagal dikirim. Coba lagi"
    }

}