package com.tokopedia.mediauploader.manager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mediauploader.services.UploaderReceiver
import com.tokopedia.mediauploader.services.UploaderReceiver.Companion.BROADCAST_CANCEL_UPLOAD
import com.tokopedia.mediauploader.services.UploaderReceiver.Companion.BROADCAST_FILE_PATH
import com.tokopedia.mediauploader.services.UploaderReceiver.Companion.BROADCAST_NOTIFICATION_ID
import com.tokopedia.mediauploader.services.UploaderReceiver.Companion.BROADCAST_SOURCE_ID
import java.io.File
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

    private val notificationBuilder = NotificationCompat
        .Builder(context, CHANNEL_GENERAL)
        .apply {
            setSmallIcon(statusBarMAIcon)
            setGroup(NOTIFICATION_GROUP)
            setOnlyAlertOnce(true)
            priority = NotificationCompat.PRIORITY_MAX
        }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_GENERAL,
                    "Upload Worker Test",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }

    fun onStart() {
        val notification = notificationBuilder
            .setContentTitle(TITLE_SUBMITTING_UPLOAD)
            .setContentText(MESSAGE_SUBMITTING_UPLOAD)
            .setStyle(NotificationCompat.BigTextStyle().bigText(MESSAGE_SUBMITTING_UPLOAD))
            .setProgress(0, 0, true)
            .setOngoing(false)
            .setShowWhen(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    @SuppressLint("UnspecifiedImmutableFlag", "RestrictedApi")
    fun onProgress(sourceId: String, file: File, progress: Int) {
        if (notificationBuilder.mActions.isEmpty()) {
            val intent = Intent(context, UploaderReceiver::class.java).apply {
                putExtra(BROADCAST_CANCEL_UPLOAD, "cancel")
                putExtra(BROADCAST_NOTIFICATION_ID, notificationId)
                putExtra(BROADCAST_SOURCE_ID, sourceId)
                putExtra(BROADCAST_FILE_PATH, file.absolutePath)
            }

            val cancelPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

            notificationBuilder.addAction(0, "Cancel", cancelPendingIntent)
        }

        val notification = notificationBuilder
            .setContentTitle(TITLE_SUBMITTING_UPLOAD)
            .setContentText(MESSAGE_SUBMITTING_UPLOAD)
            .setStyle(NotificationCompat.BigTextStyle().bigText(MESSAGE_SUBMITTING_UPLOAD))
            .setProgress(100, progress, false)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun onSuccess() {
        val notification = notificationBuilder
            .setContentTitle(TITLE_UPLOAD_SUBMITTED)
            .setContentText(MESSAGE_UPLOAD_SUBMITTED)
            .setStyle(NotificationCompat.BigTextStyle().bigText(MESSAGE_UPLOAD_SUBMITTED))
            .setProgress(100, 100, false)
            .setOngoing(false)
            .setShowWhen(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun onError() {
        val notification = notificationBuilder
            .setContentTitle(TITLE_UPLOAD_ERROR)
            .setContentText(MESSAGE_UPLOAD_ERROR)
            .setStyle(NotificationCompat.BigTextStyle().bigText(MESSAGE_UPLOAD_ERROR))
            .build()

        notificationManager.notify(notificationId, notification)
    }

    fun setId(id: Int) {
        notificationId = id
    }

    companion object {
        private const val CHANNEL_GENERAL = "ANDROID_GENERAL_CHANNEL"
        private const val NOTIFICATION_GROUP = "com.tokopedia"

        private const val TITLE_SUBMITTING_UPLOAD = "Mengirim File"
        private const val TITLE_UPLOAD_SUBMITTED = "File selesai dikirim \uD83D\uDCAB"
        private const val TITLE_UPLOAD_ERROR = "File gagal dikirim \uD83D\uDC38"
        private const val MESSAGE_SUBMITTING_UPLOAD = "Mengirim file"
        private const val MESSAGE_UPLOAD_SUBMITTED = "File terkirim"
        private const val MESSAGE_UPLOAD_ERROR = "File gagal dikirim. Coba lagi"
    }

}