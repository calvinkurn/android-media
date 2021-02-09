package com.tokopedia.topchat.chatroom.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

abstract class UploadImageNotificationManager(private val context: Context) {

    val id = System.currentTimeMillis().toInt()

    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_GENERAL).apply {
        setContentTitle(TITLE)
        setSmallIcon(com.tokopedia.design.R.drawable.ic_status_bar_notif_customerapp)
        setLargeIcon(BitmapFactory.decodeResource(context.resources, com.tokopedia.design.R.drawable.ic_big_notif_customerapp))
        setGroup(NOTIFICATION_GROUP)
        setOnlyAlertOnce(true)
        priority = NotificationCompat.PRIORITY_MAX
    }

    fun onStartUpload() {
        val text = MESSAGE
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(0, 0, true)
                .setOngoing(false)
                .setShowWhen(true)
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onSuccessUpload() {
        val text = MESSAGE_SUCCESS
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(0, 0, false)
                .setContentIntent(getSuccessIntent())
                .setOngoing(false)
                .setShowWhen(true)
                .build()

        notificationManager.cancel(TAG, id)
        notificationManager.notify(TAG, id, notification)
    }

    fun onFailedUpload(errorMessage: String) {
        val text = MESSAGE_ERROR
        val notification = notificationBuilder
                .setContentTitle(text)
                .setContentText(errorMessage)
                .setStyle(NotificationCompat.BigTextStyle().bigText(errorMessage))
                .setProgress(0, 0, false)
                .setContentIntent(getFailedIntent(errorMessage))
                .build()

        notificationManager.cancel(TAG, id)
        notificationManager.notify(TAG, id, notification)
    }

    protected abstract fun getSuccessIntent(): PendingIntent
    protected abstract fun getFailedIntent(errorMessage: String): PendingIntent

    companion object {
        private val TAG = UploadImageNotificationManager::class.java.simpleName
        private const val CHANNEL_GENERAL = "ANDROID_GENERAL_CHANNEL"
        private const val NOTIFICATION_GROUP = "com.tokopedia"
        private const val TITLE = "Upload gambar"
        private const val MESSAGE = "Mengupload gambar"
        private const val MESSAGE_SUCCESS = "Gambar berhasil di-upload"
        private const val MESSAGE_ERROR = "Gambar gagal di-upload. Coba lagi"
    }
}