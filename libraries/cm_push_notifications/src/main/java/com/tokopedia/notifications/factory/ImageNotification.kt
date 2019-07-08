package com.tokopedia.notifications.factory

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.app.NotificationCompat
import android.text.TextUtils

import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.model.BaseNotificationModel

/**
 * @author lalit.singh
 */
class ImageNotification internal constructor(context: Context, baseNotificationModel: BaseNotificationModel) : BaseNotification(context, baseNotificationModel) {

    private val blankBitmap: Bitmap
        get() {
            val w = 72
            val h = 72
            val conf = Bitmap.Config.ARGB_8888
            return Bitmap.createBitmap(w, h, conf)
        }

    override fun createNotification(): Notification {
        val builder = builder
        builder.setContentTitle(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.title))
        builder.setContentText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message))
        builder.setSmallIcon(drawableIcon)
        builder.setContentIntent(createMainPendingIntent(baseNotificationModel, requestCode))
        builder.setAutoCancel(true)
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.notificationId, requestCode))
        setBigPictureNotification(builder, baseNotificationModel)
        return builder.build()
    }

    private fun setBigPictureNotification(builder: NotificationCompat.Builder, baseNotificationModel: BaseNotificationModel) {
        val bitmap = CMNotificationUtils.loadBitmapFromUrl(baseNotificationModel.media?.mediumQuality)
        if (null != bitmap) {
            builder.setLargeIcon(bitmap)
            val bigPictureStyle = NotificationCompat.BigPictureStyle()
                    .setSummaryText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.detailMessage))
                    .bigLargeIcon(blankBitmap)
                    .bigPicture(bitmap)
            if (!TextUtils.isEmpty(baseNotificationModel.message)) {
                bigPictureStyle.setSummaryText(CMNotificationUtils
                        .getSpannedTextFromStr(baseNotificationModel.message))
            }
            builder.setStyle(bigPictureStyle)
        }
    }

}
