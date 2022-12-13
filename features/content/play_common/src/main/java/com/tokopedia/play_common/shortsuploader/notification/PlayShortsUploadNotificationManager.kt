package com.tokopedia.play_common.shortsuploader.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play_common.shortsuploader.analytic.PlayShortsUploadAnalytic
import javax.inject.Inject
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel
import com.tokopedia.play_common.shortsuploader.model.orEmpty
import com.tokopedia.play_common.shortsuploader.receiver.PlayShortsUploadReceiver
import com.tokopedia.url.TokopediaUrl
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

    fun onStart(): ForegroundInfo {
        val notification = notificationBuilder
            .setProgress(0, 0, true)
            .setContentTitle(NOTIFICATION_PROGRESS_TITLE)
            .setContentText(NOTIFICATION_PROGRESS_DESCRIPTION)
            .setStyle(NotificationCompat.BigTextStyle().bigText(NOTIFICATION_PROGRESS_DESCRIPTION))
            .setOngoing(true)
            .setShowWhen(true)
            .build()

        notificationManager.notify(notificationId, notification)

        return ForegroundInfo(notificationId, notification)
    }

    fun onProgress(progress: Int): ForegroundInfo {
        val notification = notificationBuilder
            .setProgress(PROGRESS_MAX, progress, false)
            .setContentTitle(NOTIFICATION_PROGRESS_TITLE)
            .setContentText(NOTIFICATION_PROGRESS_DESCRIPTION)
            .setStyle(NotificationCompat.BigTextStyle().bigText(NOTIFICATION_PROGRESS_DESCRIPTION))
            .setOngoing(true)
            .setShowWhen(true)
            .build()

        notificationManager.notify(notificationId, notification)

        return ForegroundInfo(notificationId, notification)
    }

    fun onSuccess(): ForegroundInfo {
        val intent = PlayShortsUploadReceiver.getIntent(
            context,
            uploadData.orEmpty(),
            PlayShortsUploadReceiver.Companion.Action.OpenPlayRoom
        )

        val openPlayRoomPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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

        val notification = notificationBuilder
            .setProgress(0, 0, false)
            .setContentTitle(NOTIFICATION_SUCCESS_TITLE)
            .setContentText(NOTIFICATION_SUCCESS_DESCRIPTION)
            .setStyle(NotificationCompat.BigTextStyle().bigText(NOTIFICATION_SUCCESS_DESCRIPTION))
            .setContentIntent(openPlayRoomPendingIntent)
            .setOngoing(false)
            .setShowWhen(true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)

        return ForegroundInfo(notificationId, notification)
    }

    fun onError(): ForegroundInfo {
        val intent = PlayShortsUploadReceiver.getIntent(
            context,
            uploadData.orEmpty(),
            PlayShortsUploadReceiver.Companion.Action.Retry
        )

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

        val notification = notificationBuilder
            .setProgress(0, 0, false)
            .setContentTitle(NOTIFICATION_FAIL_TITLE)
            .setContentText(NOTIFICATION_FAIL_DESCRIPTION)
            .setStyle(NotificationCompat.BigTextStyle().bigText(NOTIFICATION_FAIL_DESCRIPTION))
            .setOngoing(false)
            .setShowWhen(true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)

        return ForegroundInfo(notificationId, notification)
    }

    private companion object {
        const val PROGRESS_MAX = 100

        const val NOTIFICATION_GROUP = "com.tokopedia"

        const val NOTIFICATION_PROGRESS_TITLE = "Tunggu ya, videomu lagi di-upload"
        const val NOTIFICATION_PROGRESS_DESCRIPTION = "Selagi menunggu video di-upload, kamu bisa cek produk atau konten menarik lainnya di Tokopedia."

        const val NOTIFICATION_SUCCESS_TITLE = "Yay, videomu berhasil di-upload!"
        const val NOTIFICATION_SUCCESS_DESCRIPTION = "Lihat videomu di sini, yuk!"

        const val NOTIFICATION_FAIL_TITLE = "Oops, gagal upload video"
        const val NOTIFICATION_FAIL_DESCRIPTION = "Tenang, kamu masih bisa coba upload videonya lagi."
        const val NOTIFICATION_FAIL_RETRY_ACTION = "Coba lagi"

        const val CHANNEL_NAME = "Tokopedia Play Shorts"
        const val CHANNEL_DESCRIPTION = "Tokopedia Play Shorts"
        const val CHANNEL_ID = "ANDROID_GENERAL_CHANNEL"

        const val COVER_PREVIEW_SIZE = 100
    }
}
