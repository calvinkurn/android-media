package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.factory.ui.ProductWidget
import com.tokopedia.notifications.model.ActionButton
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.ProductInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class ProductNotification(
        applicationContext: Context,
        baseNotificationModel: BaseNotificationModel
) : BaseNotification(applicationContext, baseNotificationModel), ProductWidget.ProductContract {

    override fun createNotification(): Notification? {
        if (baseNotificationModel.productInfoList.isEmpty()) return null

        val productWidget = ProductWidget(
                context,
                this,
                this,
                baseNotificationModel
        )

        with(productWidget) {
            setCollapseViewData(collapsedView)
            setExpandedViewData(expandedView)
        }

        return notificationBuilder
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(productWidget.collapsedView)
                .setCustomBigContentView(productWidget.expandedView)
                .setDeleteIntent(dismissPendingIntent())
                .build()
    }

    override fun collapsedIntent(): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_PRODUCT_COLLAPSED_CLICK
        return getPendingIntent(context, intent, requestCode)
    }

    override fun productDetailIntent(product: ProductInfo): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_PRODUCT_CLICK
        intent.putExtra(CMConstant.EXTRA_PRODUCT_INFO, product)
        return getPendingIntent(context, intent, requestCode)
    }

    override fun actionButtonIntent(actionButton: ActionButton): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_BUTTON
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_EXTRA, actionButton)
        return getPendingIntent(context, intent, requestCode)
    }

    private fun dismissPendingIntent(): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_PRODUCT_NOTIFICATION_DISMISS
        return getPendingIntent(context, intent, requestCode)
    }

    override fun rightCarouselButton(remoteView: RemoteViews) {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_PRODUCT_CAROUSEL_RIGHT_CLICK
        remoteView.setOnClickPendingIntent(R.id.ivArrowRight, getPendingIntent(context, intent, requestCode))
    }

    override fun leftCarouselButton(remoteView: RemoteViews) {
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
