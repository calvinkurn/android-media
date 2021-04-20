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
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.MESSAGE_NOTIF_PRODUCT_UPLOAD
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.MESSAGE_NOTIF_PRODUCT_UPLOAD_ERROR
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.MESSAGE_NOTIF_PRODUCT_UPLOAD_SUCCESS
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.TITLE_NOTIF_PRODUCT_UPLOAD
import java.util.*

/**
 * @author by milhamj on 26/02/19.
 */
abstract class AddEditProductNotificationManager(
        private val maxCount: Int,
        protected val context: Context) {

    companion object {
        private val TAG = AddEditProductNotificationManager::class.java.simpleName
        private const val CHANNEL_GENERAL = "ANDROID_GENERAL_CHANNEL"
        private const val NOTIFICATION_GROUP = "com.tokopedia"
    }

    private val id: Int = Random().nextInt()
    private var currentProgress = 0
    private val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_GENERAL).apply {
        setContentTitle(TITLE_NOTIF_PRODUCT_UPLOAD)
        setSmallIcon(com.tokopedia.resources.common.R.drawable.ic_status_bar_notif_customerapp)
        setLargeIcon(BitmapFactory.decodeResource(context.resources, com.tokopedia.resources.common.R.drawable.ic_big_notif_customerapp))
        setGroup(NOTIFICATION_GROUP)
        setOnlyAlertOnce(true)
        priority = NotificationCompat.PRIORITY_MAX
        currentProgress = 0
    }

    fun onStartUpload(primaryImagePathOrUrl: String) {
        val text = MESSAGE_NOTIF_PRODUCT_UPLOAD
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
        val text = MESSAGE_NOTIF_PRODUCT_UPLOAD
        val notification = notificationBuilder.setContentText(text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setProgress(maxCount, currentProgress, false)
                .build()

        notificationManager.notify(TAG, id, notification)
    }

    fun onSuccessUpload() {
        val text = MESSAGE_NOTIF_PRODUCT_UPLOAD_SUCCESS
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
        val text = MESSAGE_NOTIF_PRODUCT_UPLOAD_ERROR
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