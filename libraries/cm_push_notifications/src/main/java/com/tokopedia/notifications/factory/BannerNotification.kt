package com.tokopedia.notifications.factory

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.text.format.DateUtils
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant.ReceiverAction.ACTION_BANNER_CLICK
import com.tokopedia.notifications.model.BaseNotificationModel

class BannerNotification(context: Context, baseNotificationModel: BaseNotificationModel)
    : BaseNotification(context, baseNotificationModel) {


    override fun createNotification(): Notification? {
        if (baseNotificationModel.media == null)
            return null

        val bitmap: Bitmap = getBitmap(baseNotificationModel.media?.mediumQuality)

        val style: NotificationCompat.BigPictureStyle? = NotificationCompat.BigPictureStyle()
                .setSummaryText(baseNotificationModel.message)
                .setBigContentTitle(baseNotificationModel.title)
                .bigPicture(bitmap)

        val builder = builder.apply {
            setContentTitle(baseNotificationModel.title)
            setContentText(baseNotificationModel.message)
            setLargeIcon(null)
            setCustomContentView(createCollapsedView(bitmap))
            setCustomHeadsUpContentView(createHeadsUpRemoteView(bitmap))
            setContentIntent(getPendingIntent(context, getBaseBroadcastIntent(context, baseNotificationModel).apply {
                action = ACTION_BANNER_CLICK
            }, requestCode))
            setStyle(style)
            setDeleteIntent(createDismissPendingIntent(baseNotificationModel.notificationId, requestCode))
        }

        return builder.build()
    }

    private fun createCollapsedView(bitmap: Bitmap): RemoteViews {
        val smallView: RemoteViews = if (Build.VERSION.SDK_INT > 23) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                RemoteViews(context.packageName, R.layout.cm_layout_banner_collapsed)
            else RemoteViews(context.packageName, R.layout.cm_layout_banner_collapsed_pre_dark_mode)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                RemoteViews(context.packageName, R.layout.cm_layout_banner_collapsed_marsh)
            else RemoteViews(context.packageName, R.layout.cm_layout_banner_collapsed_marsh_pre_dark_mode)
        }
        smallView.setImageViewBitmap(R.id.push_small_image, bitmap)
        smallView.setTextViewText(R.id.push_title, baseNotificationModel.title)
        smallView.setTextViewText(R.id.push_message, baseNotificationModel.message)
        smallView.setTextViewText(R.id.push_time, DateUtils.formatDateTime(context,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_DATE))
        smallView.setOnClickPendingIntent(R.id.push_noti_background,
                getPendingIntent(context, getBaseBroadcastIntent(context, baseNotificationModel).apply {
                    action = ACTION_BANNER_CLICK
                }, requestCode))
        return smallView

    }


    private fun createHeadsUpRemoteView(bitmap: Bitmap): RemoteViews {
        val headUpView: RemoteViews = if (Build.VERSION.SDK_INT > 23) {
            RemoteViews(context.packageName, R.layout.cm_layout_banner_head)
        } else {
            RemoteViews(context.packageName, R.layout.cm_layout_banner_head_pre_dark_mode)
        }
        headUpView.setImageViewBitmap(R.id.push_small_image, bitmap)
        headUpView.setTextViewText(R.id.push_title, baseNotificationModel.title)
        headUpView.setTextViewText(R.id.push_message, baseNotificationModel.message)
        headUpView.setTextViewText(R.id.push_time, DateUtils.formatDateTime(context,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_DATE))
        headUpView.setOnClickPendingIntent(R.id.push_noti_background,
                getPendingIntent(context, getBaseBroadcastIntent(context, baseNotificationModel), requestCode))
        return headUpView
    }


}