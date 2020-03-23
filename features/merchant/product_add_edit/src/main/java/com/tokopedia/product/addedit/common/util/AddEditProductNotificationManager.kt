package com.tokopedia.product.addedit.common.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.product.addedit.R
import java.io.File

/**
 * @author by milhamj on 26/02/19.
 */
abstract class AddEditProductNotificationManager(
        private val id: Int,
        private val maxCount: Int,
        private val firstImage: String,
        private val notificationManager: NotificationManager,
        protected val context: Context) {

    companion object {
        private val TAG = AddEditProductNotificationManager::class.java.simpleName
        private const val CHANNEL_GENERAL = "ANDROID_GENERAL_CHANNEL"
        private const val NOTIFICATION_GROUP = "com.tokopedia"
    }

    private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_GENERAL).apply {
        setContentTitle("Uploading")
        setSmallIcon(R.drawable.ic_status_bar_notif_customerapp)
        setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_big_notif_customerapp))
        setGroup(NOTIFICATION_GROUP)
        setOnlyAlertOnce(true)
        updateLargeIcon(this)

        setProgress(maxCount, currentProgress, false)
        setOngoing(true)
        setAutoCancel(false)

        notificationManager.notify(TAG, id, this.build())
    }

    private var currentProgress = 0
    fun onSuccessPost() {
        val text = "yessssss"
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(0, 0, false)
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentIntent(getSuccessIntent())
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onSubmitPost() {
        val text = "submit"
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(0, 0, true)
                .setOngoing(true)
                .setAutoCancel(false)
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onAddProgress() {
        currentProgress++

        val format = "%d dari %d Media…"
        val text = String.format(format, currentProgress, maxCount)
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(maxCount, currentProgress, false)
                .setOngoing(true)
                .setAutoCancel(false)
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onFailedPost(errorMessage: String) {
        val text = "error"
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

    private fun updateLargeIcon(builder: NotificationCompat.Builder) {
        val file: String = if (urlIsFile(firstImage)) {
            Uri.fromFile(File(firstImage)).toString()
        } else firstImage

        Handler(Looper.getMainLooper()).post {
            Glide.with(context.applicationContext)
                    .asBitmap()
                    .load(file)
                    .error(R.drawable.ic_big_notif_customerapp)
                    .into(object: CustomTarget<Bitmap>(100, 100) {
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            builder.setLargeIcon(resource)
                            notificationManager.notify(TAG, id, builder.build())
                        }
                    })
        }
    }
}