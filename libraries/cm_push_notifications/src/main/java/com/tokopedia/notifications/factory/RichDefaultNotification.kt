package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.text.TextUtils
import android.text.format.DateUtils
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.model.ActionButton
import com.tokopedia.notifications.model.BaseNotificationModel

class RichDefaultNotification internal constructor(
    context: Context,
    baseNotificationModel: BaseNotificationModel
) : BaseNotification(context, baseNotificationModel) {


    override fun createNotification(): Notification? {
        val builder = builder
        builder.setContentTitle(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.title))
        builder.setContentText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message))
        builder.setSmallIcon(drawableIcon)
        builder.setContentIntent(createMainPendingIntent(baseNotificationModel, requestCode))
        builder.setAutoCancel(true)
        builder.setDeleteIntent(
            createDismissPendingIntent(
                baseNotificationModel.notificationId,
                requestCode
            )
        )

        val mediaBitmap: Bitmap? = baseNotificationModel.media?.let {
            loadBitmap(baseNotificationModel.media?.mediumQuality)
        }

        if (mediaBitmap != null) {
            setBigPictureNotification(builder, baseNotificationModel, mediaBitmap)
        } else {
            setBigTextStyle(builder)
        }
        if (hasActionButton())
            setActionButton(builder)

        setNotificationIcon(builder)

        return builder.build()
    }

    private fun setNotificationIcon(builder: NotificationCompat.Builder) {
        val iconBitmap = loadBitmap(baseNotificationModel.icon)
        if (iconBitmap != null) {
            if (baseNotificationModel.isBigImage)
                builder.apply {
                    setLargeIcon(null)
                    setCustomContentView(createCollapsedView(iconBitmap))
                    setCustomHeadsUpContentView(createHeadsUpRemoteView(iconBitmap))
                }
            else
                builder.setLargeIcon(iconBitmap)
        }
    }

    private fun setBigTextStyle(builder: NotificationCompat.Builder) {
        if (!TextUtils.isEmpty(baseNotificationModel.detailMessage))
            builder.setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.detailMessage))
            )
        else if (!TextUtils.isEmpty(baseNotificationModel.message))
            builder.setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message))
            )
    }

    private fun setActionButton(builder: NotificationCompat.Builder) {
        baseNotificationModel.actionButton.forEach {
            val action = NotificationCompat.Action.Builder(
                0, it.text, getButtonPendingIntent(it)
            ).build()
            builder.addAction(action)
        }
    }

    private fun getButtonPendingIntent(actionButton: ActionButton): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_BUTTON
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_EXTRA, actionButton)
        return getPendingIntent(context, intent, requestCode)
    }

    private fun hasActionButton(): Boolean {
        return baseNotificationModel.actionButton.isNotEmpty()
    }

    private fun setBigPictureNotification(
        builder: NotificationCompat.Builder,
        baseNotificationModel: BaseNotificationModel,
        mediaBitmap: Bitmap
    ) {
        builder.setLargeIcon(mediaBitmap)

        val bigPictureStyle = NotificationCompat.BigPictureStyle()
            .setSummaryText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.detailMessage))
            .bigPicture(mediaBitmap)

        if (!TextUtils.isEmpty(baseNotificationModel.message)) {
            bigPictureStyle.setSummaryText(
                CMNotificationUtils
                    .getSpannedTextFromStr(baseNotificationModel.message)
            )
        }
        builder.setStyle(bigPictureStyle)


        val iconBitmap = if (!baseNotificationModel.icon.isNullOrBlank())
            getBitmap(baseNotificationModel.icon)
        else null

        if (iconBitmap != null)
            builder.setLargeIcon(iconBitmap)

    }


    /**
     * Remote View for Collapsed Mode and will be displayed in notification tray
     * this remote view will be used if we are receiving BaseNotification.isBigImage = true
     * && Icon is not blank and we have downloaded the image successfully
     *  */
    private fun createCollapsedView(bitmap: Bitmap): RemoteViews {
        val smallView: RemoteViews = if (Build.VERSION.SDK_INT > 23) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                RemoteViews(context.packageName, R.layout.cm_layout_banner_collapsed)
            else RemoteViews(context.packageName, R.layout.cm_layout_banner_collapsed_pre_dark_mode)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                RemoteViews(context.packageName, R.layout.cm_layout_banner_collapsed_marsh)
            else RemoteViews(
                context.packageName,
                R.layout.cm_layout_banner_collapsed_marsh_pre_dark_mode
            )
        }
        smallView.setImageViewBitmap(R.id.push_small_image, bitmap)
        smallView.setTextViewText(R.id.push_title, baseNotificationModel.title)
        smallView.setTextViewText(R.id.push_message, baseNotificationModel.message)
        smallView.setTextViewText(
            R.id.push_time, DateUtils.formatDateTime(
                context,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_DATE
            )
        )
        smallView.setOnClickPendingIntent(
            R.id.push_noti_background,
            getPendingIntent(context, getBaseBroadcastIntent(context, baseNotificationModel).apply {
                action = CMConstant.ReceiverAction.ACTION_BANNER_CLICK
            }, requestCode)
        )
        return smallView

    }

    /**
     * Remote View for Collapsed Mode and will be displayed on Screen as Heads up view not in Notification Tray
     * this remote view will be used if we are receiving BaseNotification.isBigImage = true
     * && Icon is not blank and we have downloaded the image successfully
     *  */
    private fun createHeadsUpRemoteView(bitmap: Bitmap): RemoteViews {
        val headUpView: RemoteViews = if (Build.VERSION.SDK_INT > 23) {
            RemoteViews(context.packageName, R.layout.cm_layout_banner_head)
        } else {
            RemoteViews(context.packageName, R.layout.cm_layout_banner_head_pre_dark_mode)
        }
        headUpView.setImageViewBitmap(R.id.push_small_image, bitmap)
        headUpView.setTextViewText(R.id.push_title, baseNotificationModel.title)
        headUpView.setTextViewText(R.id.push_message, baseNotificationModel.message)
        headUpView.setTextViewText(
            R.id.push_time, DateUtils.formatDateTime(
                context,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_DATE
            )
        )
        headUpView.setOnClickPendingIntent(
            R.id.push_noti_background,
            getPendingIntent(
                context,
                getBaseBroadcastIntent(context, baseNotificationModel),
                requestCode
            )
        )
        return headUpView
    }

}