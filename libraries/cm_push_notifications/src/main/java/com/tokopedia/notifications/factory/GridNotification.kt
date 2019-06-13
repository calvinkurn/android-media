package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.receiver.CMBroadcastReceiver

/**
 * @author lalit.singh
 */
class GridNotification internal constructor(context: Context, baseNotificationModel: BaseNotificationModel) : BaseNotification(context, baseNotificationModel) {

    override fun createNotification(): Notification {
        val builder = notificationBuilder
        val collapsedView = RemoteViews(context.applicationContext.packageName, R.layout.layout_collapsed)
        setCollapseData(collapsedView, baseNotificationModel)
        val expandedView = RemoteViews(context.applicationContext.packageName,
                R.layout.layout_grid_expand)
        setGridData(expandedView)
        builder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.notificationId, requestCode))
        return builder.build()
    }

    private fun setGridData(remoteViews: RemoteViews) {
        setCollapseData(remoteViews, baseNotificationModel)

        val count = baseNotificationModel.gridList.size
        val gridList = baseNotificationModel.gridList
        var bitmap: Bitmap?
        if (count >= 1) {
            bitmap = getBitmap(gridList[0].img)
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.iv_gridOne, bitmap)
            }
            gridList[0].appLink?.let {
                remoteViews.setOnClickPendingIntent(R.id.iv_gridOne,
                        getPendingIntent(requestCode, it))
            }
        }

        if (count >= 2) {
            bitmap = getBitmap(gridList[1].img)
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.iv_gridTwo, bitmap)
            }
            gridList[1].appLink?.let {
                remoteViews.setOnClickPendingIntent(R.id.iv_gridTwo,
                        getPendingIntent(requestCode, it))
            }
        }
        if (count >= 4) {
            bitmap = getBitmap(gridList[2].img)
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.iv_gridThree, bitmap)
            }
            gridList[2].appLink?.let {
                remoteViews.setOnClickPendingIntent(R.id.iv_gridThree,
                        getPendingIntent(requestCode, it))
            }

            bitmap = getBitmap(gridList[3].img)
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.iv_gridFour, bitmap)
            }
            gridList[3].appLink?.let {
                remoteViews.setOnClickPendingIntent(R.id.iv_gridFour,
                        getPendingIntent(requestCode, it))
            }
        } else {
            remoteViews.setViewVisibility(R.id.ll_bottomGridParent, View.GONE)
        }

        if (count == 6) {
            bitmap = getBitmap(gridList[4].img)
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.iv_gridFive, bitmap)
            }

            gridList[4].appLink?.let {
                remoteViews.setOnClickPendingIntent(R.id.iv_gridFive,
                        getPendingIntent(requestCode, it))
            }

            bitmap = getBitmap(gridList[5].img)
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.iv_gridSix, bitmap)
            }
            gridList[5].appLink?.let {
                remoteViews.setOnClickPendingIntent(R.id.iv_gridSix,
                        getPendingIntent(requestCode, it))
            }
        } else {
            remoteViews.setViewVisibility(R.id.iv_gridFive, View.GONE)
            remoteViews.setViewVisibility(R.id.iv_gridSix, View.GONE)
        }
    }

    private fun setCollapseData(remoteView: RemoteViews, baseNotificationModel: BaseNotificationModel) {
        if (TextUtils.isEmpty(baseNotificationModel.icon)) {
            remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, bitmapLargeIcon)
        } else {
            val iconBitmap: Bitmap? = getBitmap(baseNotificationModel.icon)
            iconBitmap?.let {
                remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, it)
            } ?: remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, bitmapLargeIcon)
        }
        remoteView.setTextViewText(R.id.tv_collapse_title, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.title))
        remoteView.setTextViewText(R.id.tv_collapsed_message, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message))
        baseNotificationModel.appLink?.let {
            remoteView.setOnClickPendingIntent(R.id.collapseMainView, getPendingIntent(requestCode,
                    it))
        }
    }


    private fun getPendingIntent(requestCode: Int, appLink: String): PendingIntent {
        val intent = Intent(context, CMBroadcastReceiver::class.java)
        intent.action = CMConstant.ReceiverAction.ACTION_GRID_CLICK
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.notificationId)
        intent.putExtra(CMConstant.EXTRA_CAMPAIGN_ID, baseNotificationModel.campaignId)
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_APP_LINK, appLink)
        return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}
