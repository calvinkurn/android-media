package com.tokopedia.notifications.factory

import android.app.Notification
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.image.downloaderFactory.ImageSizeAndTimeout
import com.tokopedia.notifications.model.BaseNotificationModel
import kotlin.math.max
import kotlin.math.min

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
        var bitmap = CMNotificationUtils.loadBitmapFromUrl(baseNotificationModel.media?.mediumQuality)
                ?: getBitmap(baseNotificationModel.media?.mediumQuality)
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

    private fun getRatio(bitmap: Bitmap): Pair<Int, Int> {
        var height = bitmap.height
        var width = bitmap.width
        val displayMetrics = Resources.getSystem().displayMetrics
        val deviceWidth = displayMetrics.widthPixels
        val notificationHeightWRTRatio = deviceWidth / 2
        val gcd = findGCD(height, width)
        width = (width / gcd) / (height / gcd) * notificationHeightWRTRatio
        height = notificationHeightWRTRatio

        return Pair(width, height)
    }

    fun findGCD(x: Int, y: Int): Int {
        var r = 0
        var a: Int
        var b: Int
        a = max(x, y) // a is greater number
        b = min(x, y) // b is smaller number
        r = b
        while (a % b != 0) {
            r = a % b
            a = b
            b = r
        }
        return r
    }

}
