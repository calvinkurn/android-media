package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.Grid

/**
 * @author lalit.singh
 */
class GridNotification internal constructor(context: Context, baseNotificationModel: BaseNotificationModel) : BaseNotification(context, baseNotificationModel) {

    override fun createNotification(): Notification {
        val builder = notificationBuilder
        val collapsedView = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            RemoteViews(context.applicationContext.packageName, R.layout.cm_layout_collapsed)
        else RemoteViews(context.applicationContext.packageName, R.layout.cm_layout_collapsed_pre_dark_mode)
        setCollapseData(collapsedView, baseNotificationModel)
        val expandedView = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            RemoteViews(context.applicationContext.packageName, R.layout.cm_layout_grid_expand)
        else RemoteViews(context.applicationContext.packageName, R.layout.cm_layout_grid_expand_pre_dark_mode)
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
            remoteViews.setImageViewBitmap(R.id.iv_gridOne, bitmap)
            gridList[0].appLink?.let {
                remoteViews.setOnClickPendingIntent(R.id.iv_gridOne,
                        getGridPendingIntent(gridList[0]))
            }
        }

        if (count >= 2) {
            bitmap = getBitmap(gridList[1].img)
            remoteViews.setImageViewBitmap(R.id.iv_gridTwo, bitmap)
            gridList[1].appLink?.let {
                remoteViews.setOnClickPendingIntent(R.id.iv_gridTwo,
                        getGridPendingIntent(gridList[1]))

            }
        }
        if (count >= 4) {
            bitmap = getBitmap(gridList[2].img)
            remoteViews.setImageViewBitmap(R.id.iv_gridThree, bitmap)
            gridList[2].appLink?.let {
                remoteViews.setOnClickPendingIntent(R.id.iv_gridThree,
                        getGridPendingIntent(gridList[2]))

            }

            bitmap = getBitmap(gridList[3].img)
            remoteViews.setImageViewBitmap(R.id.iv_gridFour, bitmap)
            gridList[3].appLink?.let {
                remoteViews.setOnClickPendingIntent(R.id.iv_gridFour,
                        getGridPendingIntent(gridList[3]))

            }
        } else {
            remoteViews.setViewVisibility(R.id.ll_bottomGridParent, View.GONE)
        }

        if (count == 6) {
            bitmap = getBitmap(gridList[4].img)
            remoteViews.setImageViewBitmap(R.id.iv_gridFive, bitmap)

            gridList[4].appLink?.let {
                remoteViews.setOnClickPendingIntent(R.id.iv_gridFive,
                        getGridPendingIntent(gridList[4]))

            }

            bitmap = getBitmap(gridList[5].img)
            remoteViews.setImageViewBitmap(R.id.iv_gridSix, bitmap)
            gridList[5].appLink?.let {
                remoteViews.setOnClickPendingIntent(R.id.iv_gridSix,
                        getGridPendingIntent(gridList[5]))
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
            remoteView.setOnClickPendingIntent(R.id.collapseMainView, getCollapsedPendingIntent())
        }
    }

    private fun getCollapsedPendingIntent(): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_GRID_MAIN_CLICK
        return getPendingIntent(context, intent, requestCode)
    }


    private fun getGridPendingIntent(grid: Grid): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_GRID_CLICK
        intent.putExtra(CMConstant.ReceiverExtraData.EXTRA_GRID_DATA, grid)
        return getPendingIntent(context, intent, requestCode)
    }

}
