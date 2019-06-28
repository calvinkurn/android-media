package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationUtils

import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.receiver.CMBroadcastReceiver

internal class ProductNotification(applicationContext: Context, baseNotificationModel: BaseNotificationModel)
    : BaseNotification(applicationContext, baseNotificationModel) {

    override fun createNotification(): Notification? {
        val builder = notificationBuilder
        if (baseNotificationModel.productInfo == null)
            return null
        val collapsedView = RemoteViews(context.applicationContext.packageName, R.layout.layout_collapsed)
        setCollapseViewData(collapsedView, baseNotificationModel)

        val expandedView = RemoteViews(context.applicationContext.packageName,
                R.layout.cm_layout_product_expand)
        setExpandViewData(expandedView)

        builder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.notificationId, requestCode))

        return builder.build()
    }

    private fun setCollapseViewData(remoteView: RemoteViews, baseNotificationModel: BaseNotificationModel) {
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
            remoteView.setOnClickPendingIntent(R.id.collapseMainView, getPendingIntent(requestCode, it))
        }
    }

    private fun setExpandViewData(remoteView: RemoteViews) {
        setCollapseViewData(remoteView, baseNotificationModel)
        val iconBitmap: Bitmap? = getBitmap(baseNotificationModel.productInfo?.productImage)
        iconBitmap?.let {
            remoteView.setImageViewBitmap(R.id.iv_productImage, it)
        } ?: remoteView.setImageViewBitmap(R.id.iv_productImage, bitmapLargeIcon)

        remoteView.setTextViewText(R.id.tv_productTitle,
                CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.productInfo?.productTitle))
        remoteView.setTextViewText(R.id.tv_oldPrice,
                CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.productInfo?.productActualPrice))
        if (baseNotificationModel.productInfo?.productActualPrice == null ||
                baseNotificationModel.productInfo?.productPriceDroppedPercentage == null) {
            remoteView.setViewVisibility(R.id.ll_oldPriceAndDiscount, View.GONE)
        } else {
            remoteView.setTextViewText(R.id.tv_oldPrice,
                    CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.productInfo?.productActualPrice))
            remoteView.setTextViewText(R.id.tv_discountPercent,
                    CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.productInfo?.productPriceDroppedPercentage))
        }
        remoteView.setTextViewText(R.id.tv_currentPrice, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.productInfo?.productCurrentPrice))
        remoteView.setTextViewText(R.id.tv_productMessage, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.productInfo?.productMessage))
        remoteView.setTextViewText(R.id.tv_productButton, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.productInfo?.productButtonMessage))
        remoteView.setOnClickPendingIntent(R.id.ll_expandedProductView, getPendingIntent(requestCode, baseNotificationModel.productInfo?.appLink!!))
    }

    private fun getPendingIntent(requestCode: Int, appLink: String): PendingIntent {
        val intent = Intent(context, CMBroadcastReceiver::class.java)
        intent.action = CMConstant.ReceiverAction.ACTION_PRODUCT_CLICK
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.notificationId)
        intent.putExtra(CMConstant.EXTRA_CAMPAIGN_ID, baseNotificationModel.campaignId)
        //todo add parent_id
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_APP_LINK, appLink)
        return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}
