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
import com.tokopedia.notifications.model.ActionButton
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.receiver.CMBroadcastReceiver

/**
 * @author lalit.singh
 */
internal class ActionNotification internal constructor(context: Context, baseNotificationModel: BaseNotificationModel) : BaseNotification(context, baseNotificationModel) {

    override fun createNotification(): Notification {
        val builder = notificationBuilder
        val collapsedView = RemoteViews(context.applicationContext.packageName, R.layout.layout_collapsed)
        setCollapseData(collapsedView, baseNotificationModel, true)

        collapsedView.setOnClickPendingIntent(R.id.collapseMainView, createMainPendingIntent(baseNotificationModel, requestCode))

        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.notificationId, requestCode))
        var expandedView = RemoteViews(context.applicationContext.packageName,
                R.layout.layout_big_image)
        if (baseNotificationModel.media == null) {
            expandedView = RemoteViews(context.applicationContext.packageName,
                    R.layout.layout_action_button)
        }

        builder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)


        baseNotificationModel.media?.let {
            it.mediumQuality.isBlank().let { isBlank ->
                if (!isBlank) {
                    expandedView.setViewVisibility(R.id.img_big, View.VISIBLE)
                    expandedView.setImageViewBitmap(R.id.img_big,
                            CMNotificationUtils.loadBitmapFromUrl(baseNotificationModel.media?.mediumQuality))
                    baseNotificationModel.actionButton.let { buttonList ->
                        if (buttonList.isNotEmpty()) {
                            expandedView.setViewVisibility(R.id.layout_collapsed, View.GONE)
                        }
                    }
                }
            }
        }


        if (CMNotificationUtils.hasActionButton(baseNotificationModel)) {
            addActionButton(baseNotificationModel.actionButton, expandedView)
        }
        setCollapseData(expandedView, baseNotificationModel, false)
        return builder.build()
    }

    private fun setCollapseData(remoteView: RemoteViews, baseNotificationModel: BaseNotificationModel, isCollapsed: Boolean) {

        if (isCollapsed) {
            if (TextUtils.isEmpty(baseNotificationModel.icon)) {
                val iconBitmap = getBitmap(baseNotificationModel.icon)
                if (null != iconBitmap) {
                    remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, iconBitmap)
                } else {
                    remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, bitmapLargeIcon)
                }
            } else if (baseNotificationModel.media != null) {
                baseNotificationModel.media?.mediumQuality?.let { imageUrl ->
                    val iconBitmap = getBitmap(imageUrl)
                    if (null != iconBitmap) {
                        remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, iconBitmap)
                    } else {
                        remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, bitmapLargeIcon)
                    }
                }
            } else {
                remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, bitmapLargeIcon)
            }
        }

        remoteView.setTextViewText(R.id.tv_collapse_title, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.title))
        remoteView.setTextViewText(R.id.tv_collapsed_message, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message))
        remoteView.setOnClickPendingIntent(R.id.collapseMainView, createMainPendingIntent(baseNotificationModel,
                requestCode))
    }

    private fun addActionButton(actionButtonList: List<ActionButton>, expandedView: RemoteViews) {
        var actionButton: ActionButton
        expandedView.setViewVisibility(R.id.ll_action, View.VISIBLE)
        for (i in actionButtonList.indices) {
            actionButton = actionButtonList[i]
            when (i) {
                0 -> {
                    expandedView.setViewVisibility(R.id.tv_button1, View.VISIBLE)
                    expandedView.setTextViewText(R.id.tv_button1, actionButton.text)
                    if (actionButton.pdActions != null) {
                        expandedView.setOnClickPendingIntent(R.id.ll_button1, getShareButtonPendingIntent(actionButton, requestCode))

                    } else {
                        expandedView.setOnClickPendingIntent(R.id.ll_button1, getButtonPendingIntent(actionButton, requestCode))

                    }
                    if (!TextUtils.isEmpty(actionButton.actionButtonIcon)) {
                        val bitmap: Bitmap? = getActionButtonBitmap(actionButton.actionButtonIcon!!)
                        if (bitmap != null) {
                            expandedView.setViewVisibility(R.id.iv_button1, View.VISIBLE)
                            expandedView.setImageViewBitmap(R.id.iv_button1, bitmap)
                        }
                    }
                }
                1 -> {
                    expandedView.setViewVisibility(R.id.tv_button2, View.VISIBLE)
                    expandedView.setTextViewText(R.id.tv_button2, actionButton.text)
                    if (actionButton.pdActions != null) {
                        expandedView.setOnClickPendingIntent(R.id.ll_button2, getShareButtonPendingIntent(actionButton, requestCode))

                    } else {
                        expandedView.setOnClickPendingIntent(R.id.ll_button2, getButtonPendingIntent(actionButton, requestCode))

                    }
                    if (!TextUtils.isEmpty(actionButton.actionButtonIcon)) {
                        val bitmap: Bitmap? = getActionButtonBitmap(actionButton.actionButtonIcon!!)
                        if (bitmap != null) {
                            expandedView.setViewVisibility(R.id.iv_button2, View.VISIBLE)
                            expandedView.setImageViewBitmap(R.id.iv_button2, bitmap)
                        }
                    }
                }
                2 -> {
                    expandedView.setViewVisibility(R.id.tv_button3, View.VISIBLE)
                    expandedView.setTextViewText(R.id.tv_button3, actionButton.text)
                    if (actionButton.pdActions != null) {
                        expandedView.setOnClickPendingIntent(R.id.ll_button3, getShareButtonPendingIntent(actionButton, requestCode))

                    } else {
                        expandedView.setOnClickPendingIntent(R.id.ll_button3, getButtonPendingIntent(actionButton, requestCode))

                    }
                    if (!TextUtils.isEmpty(actionButton.actionButtonIcon)) {
                        val bitmap: Bitmap? = getActionButtonBitmap(actionButton.actionButtonIcon!!)
                        if (bitmap != null) {
                            expandedView.setViewVisibility(R.id.iv_button3, View.VISIBLE)
                            expandedView.setImageViewBitmap(R.id.iv_button3, bitmap)
                        }
                    }
                }
            }
        }

    }


    private fun getButtonPendingIntent(actionButton: ActionButton, requestCode: Int): PendingIntent {
        val intent = Intent(context, CMBroadcastReceiver::class.java)
        intent.action = CMConstant.ReceiverAction.ACTION_BUTTON
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.notificationId)
        intent.putExtra(CMConstant.EXTRA_CAMPAIGN_ID, baseNotificationModel.campaignId)
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_APP_LINK, actionButton.appLink)
        return PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
    }

    private fun getShareButtonPendingIntent(actionButton: ActionButton, requestCode: Int): PendingIntent {
        val intent = Intent(context, CMBroadcastReceiver::class.java)
        intent.action = CMConstant.ReceiverAction.ACTION_BUTTON
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.notificationId)
        intent.putExtra(CMConstant.EXTRA_CAMPAIGN_ID, baseNotificationModel.campaignId)
        intent.putExtra(CMConstant.EXTRA_PRE_DEF_ACTION, actionButton.pdActions)
        return PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
    }


}