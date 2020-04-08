package com.tokopedia.product.addedit.common.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.product.addedit.R
import java.io.File
import java.util.*

/**
 * @author by milhamj on 26/02/19.
 */
abstract class AddEditProductNotificationManager(
        private val maxCount: Int,
        private val notificationManager: NotificationManager,
        protected val context: Context) {

    companion object {
        private val TAG = AddEditProductNotificationManager::class.java.simpleName
        private const val CHANNEL_GENERAL = "ANDROID_GENERAL_CHANNEL"
        private const val NOTIFICATION_GROUP = "com.tokopedia"
    }

    private val id: Int = Random().nextInt()

    private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_GENERAL).apply {
        setContentTitle(context.getString(R.string.title_notif_product_upload))
        setSmallIcon(R.drawable.ic_status_bar_notif_customerapp)
        setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_big_notif_customerapp))
        setGroup(NOTIFICATION_GROUP)
        setOnlyAlertOnce(true)

        setProgress(maxCount, currentProgress, false)
        setOngoing(true)
        setAutoCancel(false)

        notificationManager.notify(TAG, id, this.build())
    }

    private var currentProgress = 0
    
    fun onSuccessUpload() {
        val text = context.getString(R.string.message_notif_product_upload_success)
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(0, 0, false)
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentIntent(getSuccessIntent())
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onSubmitUpload() {
        val text = context.getString(R.string.message_notif_product_upload)
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(0, 0, true)
                .setOngoing(true)
                .setAutoCancel(false)
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onAddProgress(fileImage: File) {
        currentProgress++
        val text = context.getString(R.string.message_notif_product_upload)
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(maxCount, currentProgress, false)
                .setOngoing(true)
                .setAutoCancel(false)
        updateLargeIcon(fileImage, notification)
    }

    fun onFailedUpload(errorMessage: String) {
        val text = context.getString(R.string.message_notif_product_upload_error)
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(0, 0, false)
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentIntent(getFailedIntent(errorMessage))
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    protected abstract fun getSuccessIntent(): PendingIntent

    protected abstract fun getFailedIntent(errorMessage: String): PendingIntent

    private fun updateLargeIcon(fileImage: File, builder: NotificationCompat.Builder) {
        Handler(Looper.getMainLooper()).post {
            Glide.with(context.applicationContext)
                    .asBitmap()
                    .load(fileImage)
                    .error(R.drawable.ic_big_notif_customerapp)
                    .into(object: CustomTarget<Bitmap>(100, 100) {
                        override fun onLoadCleared(placeholder: Drawable?) {
                            // no-op
                        }
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            builder.setLargeIcon(resource)
                            notificationManager.notify(TAG, id, builder.build())
                        }
                    })
        }
    }
}