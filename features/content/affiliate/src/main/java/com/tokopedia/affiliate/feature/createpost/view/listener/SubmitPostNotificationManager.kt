package com.tokopedia.affiliate.feature.createpost.view.listener

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import com.tokopedia.affiliate.R

/**
 * @author by milhamj on 26/02/19.
 */
abstract class SubmitPostNotificationManager(
        private val id: Int,
        private val maxCount: Int,
        private val draftId: String,
        private val notificationManager: NotificationManager,
        private val context: Context) {

    companion object {
        private val TAG = SubmitPostNotificationManager::class.java.simpleName
        private const val CHANNEL_GENERAL = "ANDROID_GENERAL_CHANNEL"
        private const val NOTIFICATION_GROUP = "com.tokopedia"
    }

    private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_GENERAL).apply {
        setContentTitle("Mengunggah post di Tokopedia")
        setSmallIcon(R.drawable.ic_loading_toped)
        setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_icon_toped_announce))
        setGroup(NOTIFICATION_GROUP)
        setOnlyAlertOnce(true)
    }
    private var currentProgress = 0

    fun onAddProgress() {
        currentProgress++

        val notification = notificationBuilder.setContentText("Sedang mengunggah $currentProgress dari $maxCount media")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Sedang mengunggah $currentProgress dari $maxCount media"))
                .setProgress(maxCount, currentProgress, false)
                .setOngoing(true)
                .setAutoCancel(false)
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onSubmitPost() {
        val notification = notificationBuilder.setContentText("Sedang submit. Post Anda akan live dalam beberapa saat.")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Sedang submit. Post Anda akan live dalam beberapa saat."))
                .setProgress(0, 0, true)
                .setOngoing(true)
                .setAutoCancel(false)
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onSuccessPost() {
        val notification = notificationBuilder.setContentText("Post Anda sudah live.")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Post Anda sudah live."))
                .setProgress(0, 0, false)
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentIntent(getSuccessIntent())
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onFailedPost() {
        val notification = notificationBuilder.setContentText("Gagal mengunggah, silakan coba lagi.")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Gagal mengunggah, silakan coba lagi."))
                .setProgress(0, 0, false)
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentIntent(getFailedIntent())
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    abstract fun getSuccessIntent() : PendingIntent

    abstract fun getFailedIntent() : PendingIntent
}