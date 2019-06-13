package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.common.CarouselUtilities
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.Carousel
import com.tokopedia.notifications.receiver.CMBroadcastReceiver

/**
 * @author lalit.singh
 */
class CarouselNotification internal constructor(context: Context, baseNotificationModel: BaseNotificationModel)
    : BaseNotification(context, baseNotificationModel) {

    override fun createNotification(): Notification? {
        val builder = builder
        builder.setContentTitle(baseNotificationModel.title)
        builder.setSmallIcon(drawableIcon)
        builder.setDefaults(0)
        builder.setAutoCancel(false)

        builder.setContentTitle(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.title))
        builder.setContentText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message))

        if (baseNotificationModel.carouselList.size == 0)
            return null
        baseNotificationModel.appLink?.let {
            builder.setContentIntent(createMainPendingIntent(baseNotificationModel, requestCode))
        }

        CarouselUtilities.downloadImages(context, baseNotificationModel.carouselList)

        val remoteViews = getCarouselRemoteView(baseNotificationModel.carouselList, baseNotificationModel.carouselIndex)

        remoteViews.setOnClickPendingIntent(R.id.carouselImageMain,
                getImagePendingIntent(baseNotificationModel.carouselList[baseNotificationModel.carouselIndex], requestCode))

        builder.setCustomBigContentView(remoteViews)
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.notificationId, requestCode))
        return builder.build()
    }

    /*
     * create RemoteViews using BaseNotificationModel
     *
     * */
    private fun getCarouselRemoteView(carouselList: List<Carousel>, index: Int): RemoteViews {
        val remoteView = RemoteViews(context.packageName, R.layout.carousel_layout)
        val carousel = carouselList[index]
        carousel.index = index
        if (!TextUtils.isEmpty(carousel.text)) {
            remoteView.setViewVisibility(R.id.tvTitle, View.VISIBLE)
            remoteView.setTextViewText(R.id.tvTitle, CMNotificationUtils.getSpannedTextFromStr(carousel.text))
        } else {
            remoteView.setViewVisibility(R.id.tvTitle, View.GONE)
        }
        when (index) {
            0 -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.GONE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.VISIBLE)
                remoteView.setOnClickPendingIntent(R.id.ivArrowRight, getArrowClickPendingIntent(carouselList, requestCode, CMConstant.ReceiverAction.ACTION_RIGHT_ARROW_CLICK, index))

            }
            carouselList.size - 1 -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.GONE)
                remoteView.setOnClickPendingIntent(R.id.ivArrowLeft, getArrowClickPendingIntent(carouselList, requestCode, CMConstant.ReceiverAction.ACTION_LEFT_ARROW_CLICK, index))

            }
            else -> {
                remoteView.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE)
                remoteView.setViewVisibility(R.id.ivArrowRight, View.VISIBLE)
                remoteView.setOnClickPendingIntent(R.id.ivArrowRight, getArrowClickPendingIntent(carouselList, requestCode, CMConstant.ReceiverAction.ACTION_RIGHT_ARROW_CLICK, index))
                remoteView.setOnClickPendingIntent(R.id.ivArrowLeft, getArrowClickPendingIntent(carouselList, requestCode, CMConstant.ReceiverAction.ACTION_LEFT_ARROW_CLICK, index))

            }
        }
        remoteView.setImageViewBitmap(R.id.iv_banner, CarouselUtilities.carouselLoadImageFromStorage(carousel.filePath))
        return remoteView
    }

    private fun getArrowClickPendingIntent(carouselList: List<Carousel>, requestCode: Int, action: String, index: Int): PendingIntent {
        val resultPendingIntent: PendingIntent
        val intent = Intent(context, CMBroadcastReceiver::class.java)
        intent.action = action

        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.notificationId)
        intent.putExtra(CMConstant.EXTRA_CAMPAIGN_ID, baseNotificationModel.campaignId)

        intent.putExtra(CMConstant.PayloadKeys.CHANNEL, baseNotificationModel.channelName)
        intent.putExtra(CMConstant.PayloadKeys.UPDATE, true)
        intent.putExtra(CMConstant.PayloadKeys.CAROUSEL_INDEX, index)

        intent.putExtra(CMConstant.PayloadKeys.CAMPAIGN_ID, baseNotificationModel.campaignId)
        intent.putExtra(CMConstant.PayloadKeys.NOTIFICATION_ID, baseNotificationModel.notificationId)

        baseNotificationModel.icon?.let {
            intent.putExtra(CMConstant.PayloadKeys.ICON, baseNotificationModel.icon)
        }

        baseNotificationModel.tribeKey?.let {
            intent.putExtra(CMConstant.PayloadKeys.TRIBE_KEY, baseNotificationModel.tribeKey)
        }

        baseNotificationModel.title?.let {
            intent.putExtra(CMConstant.PayloadKeys.TITLE, baseNotificationModel.title)
        }

        baseNotificationModel.detailMessage?.let {
            intent.putExtra(CMConstant.PayloadKeys.DESCRIPTION, baseNotificationModel.detailMessage)
        }
        baseNotificationModel.message?.let {
            intent.putExtra(CMConstant.PayloadKeys.MESSAGE, baseNotificationModel.message)
        }
        baseNotificationModel.appLink?.let {
            intent.putExtra(CMConstant.PayloadKeys.APP_LINK, baseNotificationModel.appLink)

        }

        baseNotificationModel.subText?.let {
            intent.putExtra(CMConstant.PayloadKeys.SUB_TEXT, baseNotificationModel.subText)
        }


        intent.putParcelableArrayListExtra(CMConstant.ReceiverExtraData.CAROUSEL_DATA, carouselList as ArrayList<out Parcelable>)
        resultPendingIntent = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        return resultPendingIntent
    }

    private fun getImagePendingIntent(carousel: Carousel, requestCode: Int): PendingIntent {
        val resultPendingIntent: PendingIntent
        val intent = Intent(context, CMBroadcastReceiver::class.java)
        intent.action = CMConstant.ReceiverAction.ACTION_CAROUSEL_IMAGE_CLICK
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.notificationId)
        intent.putExtra(CMConstant.EXTRA_CAMPAIGN_ID, baseNotificationModel.campaignId)
        intent.putExtra(CMConstant.ReceiverExtraData.CAROUSEL_DATA_ITEM, carousel)
        intent.putExtras(getBundle(baseNotificationModel))
        resultPendingIntent = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        return resultPendingIntent
    }
}
