package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.widget.RemoteViews

import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.model.BaseNotificationModel

/**
 * @author lalit.singh
 */
class VisualNotification(context: Context, baseNotificationModel: BaseNotificationModel)
    : BaseNotification(context, baseNotificationModel) {

    override fun createNotification(): Notification? {
        val builder = builder
        builder.setSmallIcon(drawableIcon)
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.notificationId, requestCode))
        var remoteViews = RemoteViews(context.applicationContext.packageName,
                R.layout.cm_layout_visual_collapsed)

        val collapsedBitmap: Bitmap? = getBitmap(baseNotificationModel.visualCollapsedImageUrl)
        val expandedBitmap: Bitmap? = getBitmap(baseNotificationModel.visualExpandedImageUrl)

        if(collapsedBitmap == null || expandedBitmap == null)
            return null

        remoteViews.setImageViewBitmap(R.id.iv_collpasedImage, collapsedBitmap)
        remoteViews.setOnClickPendingIntent(R.id.iv_collpasedImage, getCollapsedPendingIntent())
        builder.setCustomContentView(remoteViews)

        remoteViews = RemoteViews(context.applicationContext.packageName,
                R.layout.cm_layout_visual_expand)
        remoteViews.setImageViewBitmap(R.id.iv_expanded, expandedBitmap)
        remoteViews.setOnClickPendingIntent(R.id.iv_expanded, getExpandedPendingIntent())

        builder.setCustomBigContentView(remoteViews)

        return builder.build()
    }

    private fun getCollapsedPendingIntent(): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_VISUAL_COLLAPSED_CLICK
        return getPendingIntent(context, intent, requestCode)
    }
    private fun getExpandedPendingIntent(): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_VISUAL_EXPANDED_CLICK
        return getPendingIntent(context, intent, requestCode)
    }
}
