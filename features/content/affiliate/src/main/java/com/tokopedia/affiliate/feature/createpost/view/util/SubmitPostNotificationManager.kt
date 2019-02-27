package com.tokopedia.affiliate.feature.createpost.view.util

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
        private val notificationManager: NotificationManager,
        protected val context: Context) {

    companion object {
        private val TAG = SubmitPostNotificationManager::class.java.simpleName
        private const val CHANNEL_GENERAL = "ANDROID_GENERAL_CHANNEL"
        private const val NOTIFICATION_GROUP = "com.tokopedia"
    }

    private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_GENERAL).apply {
        setContentTitle(context.getString(R.string.af_notif_uploading))
        setSmallIcon(R.drawable.ic_loading_toped)
        setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_icon_toped_announce))
        setGroup(NOTIFICATION_GROUP)
        setOnlyAlertOnce(true)
    }
    private var currentProgress = 0

    fun onAddProgress() {
        currentProgress++

        val format = context.getString(R.string.af_notif_media)
        val text = String.format(format, currentProgress, maxCount)
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(maxCount, currentProgress, false)
                .setOngoing(true)
                .setAutoCancel(false)
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onSubmitPost() {
        val text = context.getString(R.string.af_notif_submit)
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(0, 0, true)
                .setOngoing(true)
                .setAutoCancel(false)
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onSuccessPost() {
        val text = context.getString(R.string.af_notif_success)
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(0, 0, false)
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentIntent(getSuccessIntent())
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onFailedPost(errorMessage: String) {
        val text = context.getString(R.string.af_notif_error)
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(0, 0, false)
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentIntent(getFailedIntent(errorMessage))
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    protected abstract fun getSuccessIntent() : PendingIntent

    protected abstract fun getFailedIntent(errorMessage: String) : PendingIntent
}