package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.common.CarouselUtilities
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.Carousel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * @author lalit.singh
 */
class CarouselNotification internal constructor(context: Context, baseNotificationModel: BaseNotificationModel)
    : BaseNotification(context, baseNotificationModel) {

    override fun createNotification(): Notification? {
        if (baseNotificationModel.carouselList.isEmpty())
            return null

        val builder = builder
        builder.setContentTitle(baseNotificationModel.title)
                .setSmallIcon(drawableIcon)
                .setDefaults(0)
                .setAutoCancel(false)
                .setContentTitle(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.title))
                .setContentText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message))
                .setContentIntent(getCollapsedPendingIntent())

        if (!baseNotificationModel.isUpdateExisting)
            CarouselUtilities.downloadCarouselImages(context, baseNotificationModel.carouselList)

        val remoteViews = getCarouselRemoteView()
        builder.setCustomBigContentView(remoteViews)
        builder.setDeleteIntent(getDismissPendingIntent())
        return builder.build()
    }

    private fun getCarouselRemoteView(): RemoteViews {
        val remoteView = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            RemoteViews(context.packageName, R.layout.cm_carousel_layout)
        else RemoteViews(context.packageName, R.layout.cm_carousel_layout_pre_dark_mode)
        val currentCarouselItem = baseNotificationModel.carouselList[baseNotificationModel.carouselIndex]

        if (!TextUtils.isEmpty(currentCarouselItem.text)) {
            remoteView.setViewVisibility(R.id.tvTitle, View.VISIBLE)
            remoteView.setTextViewText(R.id.tvTitle, CMNotificationUtils.getSpannedTextFromStr(currentCarouselItem.text))
        } else {
            remoteView.setViewVisibility(R.id.tvTitle, View.GONE)
        }

        val productImage: Bitmap? = CarouselUtilities.loadImageFromStorage(currentCarouselItem.filePath)
        productImage?.let {
            remoteView.setImageViewBitmap(R.id.iv_banner, productImage)
        } ?: remoteView.setImageViewBitmap(R.id.iv_banner, bitmapLargeIcon)


        addLeftCarouselButton(remoteView)
        addRightCarouselButton(remoteView)

        when {
            baseNotificationModel.carouselList.size == 1 -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.INVISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.INVISIBLE)
            }
            baseNotificationModel.carouselIndex == 0 -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.INVISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.VISIBLE)
            }
            baseNotificationModel.carouselIndex == (baseNotificationModel.carouselList.size - 1) -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.INVISIBLE)
            }
            else -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.VISIBLE)
            }
        }
        remoteView.setOnClickPendingIntent(R.id.carouselImageMain, getItemPendingIntent(currentCarouselItem))
        return remoteView
    }

    private fun getCollapsedPendingIntent(): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_CAROUSEL_MAIN_CLICK
        return getPendingIntent(context, intent, requestCode)
    }

    private fun getDismissPendingIntent(): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_CAROUSEL_NOTIFICATION_DISMISS
        return getPendingIntent(context, intent, requestCode)
    }

    private fun getItemPendingIntent(carousel: Carousel): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_CAROUSEL_IMAGE_CLICK
        intent.putExtra(CMConstant.ReceiverExtraData.CAROUSEL_DATA_ITEM, carousel)
        return getPendingIntent(context, intent, requestCode)
    }

    private fun addRightCarouselButton(remoteView: RemoteViews) {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_RIGHT_ARROW_CLICK
        remoteView.setOnClickPendingIntent(R.id.ivArrowRight, getPendingIntent(context, intent, requestCode))
    }

    private fun addLeftCarouselButton(remoteView: RemoteViews) {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_LEFT_ARROW_CLICK
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
                        CarouselNotification(context, baseNotificationModel).createNotification()
                    }
                    if (notification == null) {
                        notificationManager.cancel(baseNotificationModel.notificationId)
                    } else
                        notificationManager.notify(baseNotificationModel.notificationId, notification)
                } catch (e: Exception) {
                    notificationManager.cancel(baseNotificationModel.notificationId)
                }
            }
        }
    }
}
