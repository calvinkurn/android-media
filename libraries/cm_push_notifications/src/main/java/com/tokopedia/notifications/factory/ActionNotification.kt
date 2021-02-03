package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.model.ActionButton
import com.tokopedia.notifications.model.BaseNotificationModel

/**
 * @author lalit.singh
 */
internal class ActionNotification internal constructor(context: Context, baseNotificationModel: BaseNotificationModel)
    : BaseNotification(context, baseNotificationModel) {

    private val tvButtonResIds = arrayOf(R.id.tv_button1, R.id.tv_button2, R.id.tv_button3)
    private val llButtonResIds = arrayOf(R.id.ll_button1, R.id.ll_button2, R.id.ll_button3)
    private val ivButtonResIds = arrayOf(R.id.iv_button1, R.id.iv_button2, R.id.iv_button3)


    override fun createNotification(): Notification {
        val builder = notificationBuilder
        val collapsedView = getCollapsedView()
        setCollapseData(collapsedView, baseNotificationModel, true)
        var expandedView = getExpandedView()
        collapsedView.setOnClickPendingIntent(R.id.collapseMainView,
                createMainPendingIntent(baseNotificationModel, requestCode))
        builder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)


        baseNotificationModel.media?.let {
            it.mediumQuality.isBlank().let { isBlank ->
                if (!isBlank) {
                    expandedView.setOnClickPendingIntent(R.id.img_big,
                            createMainPendingIntent(baseNotificationModel, requestCode))
                    expandedView.setViewVisibility(R.id.img_big, View.VISIBLE)
                    baseNotificationModel.media?.mediumQuality?.let { mq ->
                        if (mq.startsWith("http") || mq.startsWith("www"))
                            expandedView.setImageViewBitmap(R.id.img_big,
                                    CMNotificationUtils.loadBitmapFromUrl(baseNotificationModel.media?.mediumQuality))
                        else expandedView.setImageViewBitmap(R.id.img_big,
                                getBitmap(baseNotificationModel.media?.mediumQuality))
                    }
                    baseNotificationModel.actionButton.isNotEmpty().let { isNonEmpty ->
                        if (isNonEmpty)
                            expandedView.setViewVisibility(R.id.layout_collapsed, View.GONE)
                    }
                }
            }
        }

        if (CMNotificationUtils.hasActionButton(baseNotificationModel)) {
            addActionButton(baseNotificationModel.actionButton, expandedView)
        }
        setCollapseData(expandedView, baseNotificationModel, false)
        return builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.notificationId, requestCode))
                .build()
    }

    private fun getExpandedView(): RemoteViews {
        var expandedView: RemoteViews
        expandedView = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            RemoteViews(context.applicationContext.packageName,
                    R.layout.cm_layout_big_image)
        else RemoteViews(context.applicationContext.packageName,
                R.layout.cm_layout_big_image_pre_dark_mode)
        if (baseNotificationModel.media == null) {
            expandedView = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                RemoteViews(context.applicationContext.packageName,
                        R.layout.cm_layout_action_button)
            else RemoteViews(context.applicationContext.packageName,
                    R.layout.cm_layout_action_button_pre_dark_mode)

        }
        return expandedView
    }

    private fun getCollapsedView(): RemoteViews {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            RemoteViews(context.applicationContext.packageName, R.layout.cm_layout_collapsed)
        else
            RemoteViews(context.applicationContext.packageName, R.layout.cm_layout_collapsed_pre_dark_mode)
    }

    private fun setCollapseData(remoteView: RemoteViews, baseNotificationModel: BaseNotificationModel, isCollapsed: Boolean) {
        if (isCollapsed) {
            when {
                !TextUtils.isEmpty(baseNotificationModel.icon) -> {
                    val iconBitmap = getBitmap(baseNotificationModel.icon)
                    remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, iconBitmap)
                }
                baseNotificationModel.media != null -> baseNotificationModel.media?.mediumQuality?.let { imageUrl ->
                    val iconBitmap = getBitmap(imageUrl)
                    remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, iconBitmap)
                }
                else -> remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, bitmapLargeIcon)
            }
        } else if (!TextUtils.isEmpty(baseNotificationModel.icon) && baseNotificationModel.media == null) {
            val iconBitmap = getBitmap(baseNotificationModel.icon)
            remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, iconBitmap)
        }

        remoteView.setTextViewText(R.id.tv_collapse_title, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.title))
        remoteView.setTextViewText(R.id.tv_collapsed_message, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.message))
        remoteView.setOnClickPendingIntent(if (isCollapsed) R.id.collapseMainView else R.id.status_bar_latest_event_content, createMainPendingIntent(baseNotificationModel,
                requestCode))
        if (baseNotificationModel.media == null || baseNotificationModel.media?.mediumQuality == null
                || TextUtils.isEmpty(baseNotificationModel.media?.mediumQuality)) {
            remoteView.setViewVisibility(if (isCollapsed) R.id.tv_collapsed_message else R.id.tv_expanded_message, View.VISIBLE)
            remoteView.setViewVisibility(if (isCollapsed) R.id.tv_expanded_message else R.id.tv_collapsed_message, View.GONE)
            remoteView.setTextViewText(if (isCollapsed) R.id.tv_collapsed_message else R.id.tv_expanded_message,
                    CMNotificationUtils.getSpannedTextFromStr(if (isCollapsed) baseNotificationModel.message
                    else if (!TextUtils.isEmpty(baseNotificationModel.detailMessage)) baseNotificationModel.detailMessage
                    else baseNotificationModel.message))
        }
    }

    private fun addActionButton(actionButtonList: List<ActionButton>, expandedView: RemoteViews) {
        var actionButton: ActionButton
        expandedView.setViewVisibility(R.id.ll_action, View.VISIBLE)
        for (i in actionButtonList.indices) {
            actionButton = actionButtonList[i]
            expandedView.setViewVisibility(tvButtonResIds[i], View.VISIBLE)
            expandedView.setTextViewText(tvButtonResIds[i], actionButton.text)
            expandedView.setOnClickPendingIntent(llButtonResIds[i], getButtonPendingIntent(actionButton))
            if (!TextUtils.isEmpty(actionButton.actionButtonIcon)) {
                val bitmap: Bitmap? = getActionButtonBitmap(actionButton.actionButtonIcon!!)
                if (bitmap != null) {
                    expandedView.setViewVisibility(ivButtonResIds[i], View.VISIBLE)
                    expandedView.setImageViewBitmap(ivButtonResIds[i], bitmap)
                }
            }
        }
    }

    private fun getButtonPendingIntent(actionButton: ActionButton): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_BUTTON
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_EXTRA, actionButton)
        return getPendingIntent(context, intent, requestCode)
    }


}
