package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.tokopedia.notifications.R
import com.tokopedia.notifications.analytics.ProductAnalytics
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMConstant.NotificationProductType
import com.tokopedia.notifications.common.CarouselUtilities
import com.tokopedia.notifications.model.ActionButton
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.ProductInfo
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import com.tokopedia.notifications.common.CMNotificationUtils.getSpannedTextFromStr as spanStr
import com.tokopedia.notifications.common.CarouselUtilities.loadImageFromStorage as loadFileImage

internal class ProductNotification(
        applicationContext: Context,
        baseNotificationModel: BaseNotificationModel
) : BaseNotification(applicationContext, baseNotificationModel) {

    private val userSession by lazy { UserSession(context) }

    override fun createNotification(): Notification? {
        val builder = notificationBuilder

        if (baseNotificationModel.productInfoList.isEmpty()) return null
        if (!baseNotificationModel.isUpdateExisting) {
            CarouselUtilities.downloadProductImages(context.applicationContext, baseNotificationModel.productInfoList)
        }

        val currentProductInfo = baseNotificationModel.productInfoList[baseNotificationModel.carouselIndex]
        val productImage: Bitmap? = loadFileImage(currentProductInfo.productImage)

        val collapsedView = RemoteViews(context.applicationContext.packageName, R.layout.cm_layout_collapsed)
        setCollapseViewData(collapsedView)
        val expandedView = RemoteViews(context.applicationContext.packageName, R.layout.cm_layout_product_expand)
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
        remoteView.setTextViewText(R.id.tv_collapse_title, spanStr(baseNotificationModel.title))
        remoteView.setTextViewText(R.id.tv_collapsed_message, spanStr(baseNotificationModel.message))
        remoteView.setOnClickPendingIntent(R.id.collapseMainView, getCollapsedPendingIntent())
    }

    private fun setExpandViewData(remoteView: RemoteViews, currentProductInfo: ProductInfo, bitmap: Bitmap?) {
        setCollapseViewData(remoteView)
        bitmap?.let {
            remoteView.setImageViewBitmap(R.id.iv_productImage, it)
        } ?: remoteView.setImageViewBitmap(R.id.iv_productImage, bitmapLargeIcon)

        remoteView.setTextViewText(R.id.tv_productTitle,
                spanStr(currentProductInfo.productTitle))
        remoteView.setTextViewText(R.id.tv_oldPrice,
                spanStr(currentProductInfo.productActualPrice))

        if (currentProductInfo.productActualPrice == null ||
                currentProductInfo.productPriceDroppedPercentage == null) {
            remoteView.setViewVisibility(R.id.ll_oldPriceAndDiscount, View.GONE)
        } else {
            remoteView.setTextViewText(R.id.tv_oldPrice,
                    spanStr("<strike>${currentProductInfo.productActualPrice}</strike>"))
            remoteView.setTextViewText(R.id.tv_discountPercent,
                    spanStr(currentProductInfo.productPriceDroppedPercentage))
        }

        remoteView.setTextViewText(R.id.tv_currentPrice, spanStr(currentProductInfo.productCurrentPrice))
        remoteView.setOnClickPendingIntent(R.id.ll_expandedProductView, getProductPendingIntent(currentProductInfo))

        addLeftCarouselButton(remoteView)
        addRightCarouselButton(remoteView)

        when (baseNotificationModel.notificationProductType) {
            NotificationProductType.V2 -> productDetailCard(remoteView, currentProductInfo)
            else -> productStockCard(remoteView, currentProductInfo)
        }
    }

    private fun productStockCard(remoteView: RemoteViews, product: ProductInfo) {
        remoteView.setTextViewText(R.id.tv_productButton, spanStr(product.productButtonMessage))
        remoteView.setTextViewText(R.id.tv_productMessage, spanStr(product.productMessage))

        // visibility
        when {
            baseNotificationModel.productInfoList.size == 1 -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.INVISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.INVISIBLE)
            }
            baseNotificationModel.carouselIndex == 0 -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.INVISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.VISIBLE)
            }
            baseNotificationModel.carouselIndex == (baseNotificationModel.productInfoList.size - 1) -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.INVISIBLE)
            }
            else -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.VISIBLE)
            }
        }
    }

    private fun productDetailCard(remoteView: RemoteViews, product: ProductInfo) {
        //tracker
        ProductAnalytics.impression(userSession.userId, baseNotificationModel, product)
        ProductAnalytics.impressionExpanded(
                userSession.userId,
                baseNotificationModel,
                product
        )

        // expand
        val actionButton = product.actionButton[baseNotificationModel.carouselIndex]

        if (product.freeOngkirIcon.isNullOrEmpty()) {
            remoteView.setViewVisibility(R.id.img_campaign, View.GONE)
        } else {
            remoteView.setViewVisibility(R.id.img_campaign, View.VISIBLE)
            loadFileImage(product.freeOngkirIcon)?.let {
                remoteView.setImageViewBitmap(R.id.img_campaign, it)
            }
        }

        // visibility
        remoteView.setViewVisibility(R.id.tv_productMessage, View.GONE)
        remoteView.setViewVisibility(R.id.ivArrowLeft, View.GONE)
        remoteView.setViewVisibility(R.id.ivArrowRight, View.GONE)

        // action button
        remoteView.setOnClickPendingIntent(R.id.tv_productButton, getButtonPendingIntent(actionButton))
        remoteView.setOnClickPendingIntent(R.id.iv_productImage, getProductPendingIntent(product))
        remoteView.setOnClickPendingIntent(R.id.ll_content, getProductPendingIntent(product))
        remoteView.setTextViewText(R.id.tv_productButton, actionButton.text)
    }

    private fun getButtonPendingIntent(actionButton: ActionButton): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_BUTTON
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_EXTRA, actionButton)
        return getPendingIntent(context, intent, requestCode)
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
