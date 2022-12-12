package com.tokopedia.play_common.shortsuploader.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import javax.inject.Inject
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel
import com.tokopedia.play_common.shortsuploader.model.orEmpty
import com.tokopedia.play_common.shortsuploader.receiver.PlayShortsUploadReceiver
import kotlinx.coroutines.withContext

/**
 * Created By : Jonathan Darwin on December 07, 2022
 */
class PlayShortsUploadNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers,
){

    private val notificationManager = context.getSystemService(
        Context.NOTIFICATION_SERVICE
    ) as NotificationManager

    private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
        setDefaults(Notification.DEFAULT_SOUND)
        setOnlyAlertOnce(true)
        /** TODO: adjust smallIcon here */
        setSmallIcon(com.tokopedia.resources.common.R.drawable.ic_status_bar_notif_customerapp)
        setGroup(NOTIFICATION_GROUP)
        priority = NotificationCompat.PRIORITY_HIGH
    }

    private var uploadData: PlayShortsUploadModel? = null

    private val notificationId: Int
        get() = uploadData?.shortsId.toIntOrZero()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    suspend fun init(uploadData: PlayShortsUploadModel) {
        this.uploadData = uploadData

        withContext(dispatchers.io) {
            try {
                val bitmap = Glide.with(context)
                    .asBitmap()
                    .load(uploadData.coverUri.ifEmpty { uploadData.mediaUri })
                    .submit(COVER_PREVIEW_SIZE, COVER_PREVIEW_SIZE)
                    .get()

                notificationBuilder.setLargeIcon(bitmap)
            }
            catch (e: Exception) {

            }
        }
    }

    fun onStart() {
        val builder = notificationBuilder
            .setProgress(0, 0, true)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_PROGRESS_DESCRIPTION)
            .setStyle(NotificationCompat.BigTextStyle().bigText(NOTIFICATION_PROGRESS_DESCRIPTION))
            .setOngoing(true)
            .setShowWhen(true)
            .build()

        notificationManager.notify(notificationId, builder)
    }

    fun onProgress(progress: Int) {
        val builder = notificationBuilder
            .setProgress(PROGRESS_MAX, progress, false)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_PROGRESS_DESCRIPTION)
            .setStyle(NotificationCompat.BigTextStyle().bigText(NOTIFICATION_PROGRESS_DESCRIPTION))
            .setOngoing(true)
            .setShowWhen(true)
            .build()

        notificationManager.notify(notificationId, builder)
    }

    fun onSuccess() {
        val intent = RouteManager.getIntent(context, ApplinkConst.PLAY_DETAIL, uploadData?.shortsId.orEmpty())

        val openPlayRoomPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }

        val builder = notificationBuilder
            .setProgress(0, 0, false)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_SUCCESS_DESCRIPTION)
            .setStyle(NotificationCompat.BigTextStyle().bigText(NOTIFICATION_SUCCESS_DESCRIPTION))
            .setContentIntent(openPlayRoomPendingIntent)
            .setOngoing(false)
            .setShowWhen(true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, builder)
    }

    fun onError() {
        val intent = PlayShortsUploadReceiver.getIntent(context, uploadData.orEmpty())

        val retryPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }

        notificationBuilder.addAction(0, NOTIFICATION_FAIL_RETRY_ACTION, retryPendingIntent)

        val builder = notificationBuilder
            .setProgress(0, 0, false)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_FAIL_DESCRIPTION)
            .setStyle(NotificationCompat.BigTextStyle().bigText(NOTIFICATION_FAIL_DESCRIPTION))
            .setOngoing(false)
            .setShowWhen(true)
            .build()

        notificationManager.notify(notificationId, builder)
    }


    private companion object {
        const val PROGRESS_MAX = 100

        const val NOTIFICATION_GROUP = "com.tokopedia"
        /** TODO: change title & description */
        const val NOTIFICATION_TITLE = "Tokopedia"
        const val NOTIFICATION_PROGRESS_DESCRIPTION = "Memproses..."
        const val NOTIFICATION_SUCCESS_DESCRIPTION = "Berhasil di-upload"
        const val NOTIFICATION_FAIL_DESCRIPTION = "Gagal upload"
        const val NOTIFICATION_FAIL_RETRY_ACTION = "Coba lagi"

        const val CHANNEL_NAME = "Tokopedia Play Shorts"
        const val CHANNEL_DESCRIPTION = "Tokopedia Play Shorts"
        const val CHANNEL_ID = "ANDROID_GENERAL_CHANNEL"


        const val COVER_PREVIEW_SIZE = 100
    }
}
