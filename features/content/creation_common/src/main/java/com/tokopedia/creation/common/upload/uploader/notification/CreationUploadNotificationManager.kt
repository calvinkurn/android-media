package com.tokopedia.creation.common.upload.uploader.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadNotificationText
import com.tokopedia.creation.common.upload.model.CreationUploadSuccessData
import com.tokopedia.creation.common.upload.uploader.receiver.CreationUploadReceiver
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.getBitmapFromUrl
import kotlinx.coroutines.withContext
import com.tokopedia.resources.common.R as resourcescommonR

/**
 * Created By : Jonathan Darwin on September 18, 2023
 */
abstract class CreationUploadNotificationManager(
    private val context: Context,
    private val dispatchers: CoroutineDispatchers,
    private val gson: Gson,
){

    private val notificationManager = context.getSystemService(
        Context.NOTIFICATION_SERVICE
    ) as NotificationManager

    private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
        setDefaults(Notification.DEFAULT_SOUND)
        setOnlyAlertOnce(true)
        setSmallIcon(resourcescommonR.drawable.ic_status_bar_notif_customerapp)
        setGroup(NOTIFICATION_GROUP)
        priority = NotificationCompat.PRIORITY_HIGH
    }

    protected var uploadData: CreationUploadData? = null

    private var notificationId: Int = -1

    private val notificationIdAfterUpload: Int
        get() = uploadData?.notificationIdAfterUpload.orZero()

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

    abstract val uploadNotificationText: CreationUploadNotificationText

    abstract fun generateSuccessPendingIntent(successData: CreationUploadSuccessData): PendingIntent?

    private fun generatePendingIntentToReceiver(action: CreationUploadReceiver.Action): PendingIntent {
        val intent = CreationUploadReceiver.getIntent(
            context,
            uploadData?.mapToJson(gson).orEmpty(),
            action
        )

        val requestCode = (0..1000000).random()

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    suspend fun init(
        uploadData: CreationUploadData,
        notificationId: Int,
    ) {
        this.uploadData = uploadData
        this.notificationId = notificationId

        withContext(dispatchers.io) {
            try {
                val bitmap = uploadData.notificationCover.getBitmapFromUrl(context) {
                    overrideSize(Resize(COVER_PREVIEW_SIZE, COVER_PREVIEW_SIZE))
                }

                notificationBuilder.setLargeIcon(bitmap)
            }
            catch (_: Exception) {

            }
        }
    }

    fun onStart(): ForegroundInfo {
        val notification = notificationBuilder
            .setProgress(0, 0, true)
            .setContentTitle(uploadNotificationText.progressTitle)
            .setContentText(uploadNotificationText.progressDescription)
            .setStyle(NotificationCompat.BigTextStyle().bigText(uploadNotificationText.progressDescription))
            .setOngoing(true)
            .setShowWhen(true)
            .build()

        notificationManager.notify(notificationId, notification)

        return ForegroundInfo(notificationId, notification)
    }

    fun onProgress(progress: Int): ForegroundInfo {
        val notification = notificationBuilder
            .setProgress(PROGRESS_MAX, progress, false)
            .setContentTitle(uploadNotificationText.progressTitle)
            .setContentText(uploadNotificationText.progressDescription)
            .setStyle(NotificationCompat.BigTextStyle().bigText(uploadNotificationText.progressDescription))
            .setOngoing(true)
            .setShowWhen(true)
            .build()

        notificationManager.notify(notificationId, notification)

        return ForegroundInfo(notificationId, notification)
    }

    fun onSuccess(successData: CreationUploadSuccessData): ForegroundInfo {
        val successPendingIntent = generateSuccessPendingIntent(successData)

        val notification = notificationBuilder
            .setProgress(0, 0, false)
            .setContentTitle(uploadNotificationText.successTitle)
            .setContentText(uploadNotificationText.successDescription)
            .setStyle(NotificationCompat.BigTextStyle().bigText(uploadNotificationText.successDescription))
            .setContentIntent(successPendingIntent)
            .setOngoing(false)
            .setShowWhen(true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationIdAfterUpload, notification)

        return ForegroundInfo(notificationIdAfterUpload, notification)
    }

    fun onError(): ForegroundInfo {
        val errorPendingIntent = generatePendingIntentToReceiver(CreationUploadReceiver.Action.Retry)

        val deleteNotificationPendingIntent = generatePendingIntentToReceiver(CreationUploadReceiver.Action.RemoveQueue)

        notificationBuilder.addAction(0, uploadNotificationText.failRetryAction, errorPendingIntent)

        val notification = notificationBuilder
            .setProgress(0, 0, false)
            .setContentTitle(uploadNotificationText.failTitle)
            .setContentText(uploadNotificationText.failDescription)
            .setStyle(NotificationCompat.BigTextStyle().bigText(uploadNotificationText.failDescription))
            .setDeleteIntent(deleteNotificationPendingIntent)
            .setOngoing(false)
            .setShowWhen(true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationIdAfterUpload, notification)

        return ForegroundInfo(notificationIdAfterUpload, notification)
    }

    private companion object {
        const val PROGRESS_MAX = 100

        const val NOTIFICATION_GROUP = "com.tokopedia"

        const val CHANNEL_NAME = "Tokopedia Content Creation"
        const val CHANNEL_DESCRIPTION = "Tokopedia Content Creation"
        const val CHANNEL_ID = "ANDROID_GENERAL_CHANNEL"

        const val COVER_PREVIEW_SIZE = 100
    }
}
