package com.tokopedia.notifications.factory

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.widget.RemoteViews

import com.tokopedia.notifications.R
import com.tokopedia.notifications.model.BaseNotificationModel

/**
 * @author lalit.singh
 */
class VisualNotification(context: Context, baseNotificationModel: BaseNotificationModel)
    : BaseNotification(context, baseNotificationModel) {

    override fun createNotification(): Notification? {
        val builder = builder
        builder.setSmallIcon(drawableIcon)
        builder.setContentIntent(createMainPendingIntent(baseNotificationModel, requestCode))
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.notificationId, requestCode))
        var remoteViews = RemoteViews(context.applicationContext.packageName,
                R.layout.layout_visual_collapsed)

        val collapsedBitmap: Bitmap? = getBitmap(baseNotificationModel.visualCollapsedImageUrl)
        val expandedBitmap: Bitmap? = getBitmap(baseNotificationModel.visualExpandedImageUrl)

        if(collapsedBitmap == null || expandedBitmap == null)
            return null

        remoteViews.setImageViewBitmap(R.id.iv_collpasedImage, collapsedBitmap)
        builder.setCustomContentView(remoteViews)

        remoteViews = RemoteViews(context.applicationContext.packageName,
                R.layout.layout_visual_expand)
        remoteViews.setImageViewBitmap(R.id.iv_expanded, expandedBitmap)

        builder.setCustomBigContentView(remoteViews)

        return builder.build()
    }
}
