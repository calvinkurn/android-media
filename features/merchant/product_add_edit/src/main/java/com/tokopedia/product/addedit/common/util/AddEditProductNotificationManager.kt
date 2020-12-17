package com.tokopedia.product.addedit.common.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.core.app.NotificationCompat
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.addedit.R
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
    private var currentProgress = 0

    private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_GENERAL).apply {
        setContentTitle(context.getString(com.tokopedia.product.addedit.R.string.title_notif_product_upload))
        setSmallIcon(R.drawable.ic_status_bar_notif_customerapp)
        setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_big_notif_customerapp))
        setGroup(NOTIFICATION_GROUP)
        setOnlyAlertOnce(true)
        priority = NotificationCompat.PRIORITY_MAX
    }
    
    fun onSuccessUpload() {
        val text = context.getString(R.string.message_notif_product_upload_success)
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(0, 0, false)
                .setContentIntent(getSuccessIntent())
                .setOngoing(false)
                .setShowWhen(true)
                .build()
        notificationManager.notify(TAG, id, notification)
    }

    fun onStartUpload(primaryImagePathOrUrl: String) {
        val text = context.getString(R.string.message_notif_product_upload)
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(0, 0, true)
                .setOngoing(false)
                .setShowWhen(true)
                .build()

        notificationManager.notify(TAG, id, notification)
        updateLargeIcon(primaryImagePathOrUrl)
    }

    fun onAddProgress() {
        currentProgress++
        val text = context.getString(R.string.message_notif_product_upload)
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(maxCount, currentProgress, false)
                .setOngoing(true)
                .setShowWhen(true)
                .build()

        notificationManager.notify(TAG, id, notification)
    }

    fun onFailedUpload(errorMessage: String) {
        val text = context.getString(R.string.message_notif_product_upload_error)
        val notification = notificationBuilder
                .setContentTitle(text)
                .setContentText(errorMessage)
                .setStyle(NotificationCompat.BigTextStyle().bigText(errorMessage))
                .setProgress(0, 0, false)
                .setContentIntent(getFailedIntent(errorMessage))
                .setOngoing(false)
                .setShowWhen(true)
                .build()

        notificationManager.notify(TAG, id, notification)
    }

    protected abstract fun getSuccessIntent(): PendingIntent

    protected abstract fun getFailedIntent(errorMessage: String): PendingIntent

    private fun updateLargeIcon(primaryImagePathOrUrl: String) {
        val target = object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) {
                //no-op
            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val notification = notificationBuilder.setLargeIcon(resource).build()
                notificationManager.notify(TAG, id, notification)
            }
        }
        ImageHandler.loadImageWithTarget(
                context,
                primaryImagePathOrUrl,
                target
        )
    }
}