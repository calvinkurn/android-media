package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.common.CarouselUtilities
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.ProductInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class ProductNotification(applicationContext: Context, baseNotificationModel: BaseNotificationModel)
    : BaseNotification(applicationContext, baseNotificationModel) {

    override fun createNotification(): Notification? {
        val builder = notificationBuilder
        if (baseNotificationModel.productInfoList == null || baseNotificationModel.productInfoList!!.isEmpty())
            return null
        if (!baseNotificationModel.isUpdateExisting)
            CarouselUtilities.downloadProductImages(context.applicationContext, baseNotificationModel.productInfoList!!)

        val currentProductInfo = baseNotificationModel.productInfoList!![baseNotificationModel.carouselIndex]
        val productImage: Bitmap? = CarouselUtilities.loadImageFromStorage(currentProductInfo.productImage)

        val collapsedView = RemoteViews(context.applicationContext.packageName, R.layout.layout_collapsed)
        setCollapseViewData(collapsedView)

        val expandedView = RemoteViews(context.applicationContext.packageName,
                R.layout.cm_layout_product_expand)
        setExpandViewData(expandedView, currentProductInfo, productImage)
        builder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
        builder.setDeleteIntent(getDismissPendingIntent())

        return builder.build()
    }

    private fun setCollapseViewData(remoteView: RemoteViews) {
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

    private fun setExpandViewData(remoteView: RemoteViews, currentProductInfo: ProductInfo, bitmap: Bitmap?) {
        setCollapseViewData(remoteView)
        bitmap?.let {
            remoteView.setImageViewBitmap(R.id.iv_productImage, it)
        } ?: remoteView.setImageViewBitmap(R.id.iv_productImage, bitmapLargeIcon)

        remoteView.setTextViewText(R.id.tv_productTitle,
                CMNotificationUtils.getSpannedTextFromStr(currentProductInfo.productTitle))
        remoteView.setTextViewText(R.id.tv_oldPrice,
                CMNotificationUtils.getSpannedTextFromStr(currentProductInfo.productActualPrice))

        if (currentProductInfo.productActualPrice == null ||
                currentProductInfo.productPriceDroppedPercentage == null) {
            remoteView.setViewVisibility(R.id.ll_oldPriceAndDiscount, View.GONE)
        } else {
            remoteView.setTextViewText(R.id.tv_oldPrice,
                    CMNotificationUtils.getSpannedTextFromStr("<strike>${currentProductInfo.productActualPrice}</strike>"))
            remoteView.setTextViewText(R.id.tv_discountPercent,
                    CMNotificationUtils.getSpannedTextFromStr(currentProductInfo.productPriceDroppedPercentage))
        }
        remoteView.setTextViewText(R.id.tv_currentPrice, CMNotificationUtils.getSpannedTextFromStr(currentProductInfo.productCurrentPrice))
        remoteView.setTextViewText(R.id.tv_productMessage, CMNotificationUtils.getSpannedTextFromStr(currentProductInfo.productMessage))
        remoteView.setTextViewText(R.id.tv_productButton,
                CMNotificationUtils.getSpannedTextFromStr(currentProductInfo.productButtonMessage))
        remoteView.setOnClickPendingIntent(R.id.ll_expandedProductView, getProductPendingIntent(currentProductInfo))

        addLeftCarouselButton(remoteView)
        addRightCarouselButton(remoteView)

        when {
            baseNotificationModel.productInfoList!!.size == 1 -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.INVISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.INVISIBLE)
            }
            baseNotificationModel.carouselIndex == 0 -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.INVISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.VISIBLE)
            }
            baseNotificationModel.carouselIndex == (baseNotificationModel.productInfoList!!.size - 1) -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.INVISIBLE)
            }
            else -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.VISIBLE)
            }
        }
    }

    private fun getCollapsedPendingIntent(): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_PRODUCT_COLLAPSED_CLICK
        return getPendingIntent(context, intent, requestCode)
    }

    private fun getDismissPendingIntent(): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_PRODUCT_NOTIFICATION_DISMISS
        return getPendingIntent(context, intent, requestCode)
    }

    private fun getProductPendingIntent(productInfo: ProductInfo): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_PRODUCT_CLICK
        intent.putExtra(CMConstant.EXTRA_PRODUCT_INFO, productInfo)
        return getPendingIntent(context, intent, requestCode)
    }

    private fun addRightCarouselButton(remoteView: RemoteViews) {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_PRODUCT_CAROUSEL_RIGHT_CLICK
        remoteView.setOnClickPendingIntent(R.id.ivArrowRight, getPendingIntent(context, intent, requestCode))
    }

    private fun addLeftCarouselButton(remoteView: RemoteViews) {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_PRODUCT_CAROUSEL_LEFT_CLICK
        remoteView.setOnClickPendingIntent(R.id.ivArrowLeft, getPendingIntent(context, intent, requestCode))
    }

    companion object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main

        fun onLeftIconClick(context: Context, baseNotificationModel: BaseNotificationModel) {
            baseNotificationModel.carouselIndex = baseNotificationModel.carouselIndex - 1
            postNotification(context, baseNotificationModel)
        }

        fun onRightIconClick(context: Context, baseNotificationModel: BaseNotificationModel) {
            baseNotificationModel.carouselIndex = baseNotificationModel.carouselIndex + 1
            postNotification(context, baseNotificationModel)
        }

        private fun postNotification(context: Context, baseNotificationModel: BaseNotificationModel) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            launch {
                try {
                    baseNotificationModel.isUpdateExisting = true
                    val notification: Notification? = withContext(Dispatchers.IO) {
                        ProductNotification(context, baseNotificationModel).createNotification()
                    }
                    if (notification == null) {
                        notificationManager.cancel(baseNotificationModel.notificationId)
                    } else
                        notificationManager.notify(baseNotificationModel.notificationId, notification)
                } catch (e: Throwable) {
                    notificationManager.cancel(baseNotificationModel.notificationId)
                }
            }
        }
    }

}
