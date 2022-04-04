package com.tokopedia.notifications.factory

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant.ReviewStarNumber.*
import com.tokopedia.notifications.common.CMConstant.ReceiverAction.ACTION_REVIEW_NOTIFICATION_STAR_CLICKED
import com.tokopedia.notifications.common.CMNotificationUtils.getSpannedTextFromStr
import com.tokopedia.notifications.model.BaseNotificationModel


class ReviewNotification internal constructor(
    context: Context,
    baseNotificationModel: BaseNotificationModel
) : BaseNotification(context, baseNotificationModel) {

    private val packageName = context.applicationContext.packageName

    private val collapsedView by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            RemoteViews(packageName, R.layout.cm_layout_review_collapsed)
        else RemoteViews(packageName, R.layout.cm_layout_review_collapsed_pre_dark_mode)
    }
    private val expandedView by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            RemoteViews(packageName, R.layout.cm_layout_review_expand)
        else RemoteViews(packageName, R.layout.cm_layout_review_expand_pre_dark_mode)
    }

    override fun createNotification(): Notification? {
        val builder = builder
        setupUI(builder)
        builder.setContentIntent(createMainPendingIntent(baseNotificationModel, requestCode))
               .setStyle(NotificationCompat.DecoratedCustomViewStyle())
               .setCustomContentView(collapsedView)
               .setCustomBigContentView(expandedView)
               .setDeleteIntent(
            createDismissPendingIntent(
                baseNotificationModel.notificationId,
                requestCode
            )
        )
        handleReview(collapsedView)
        handleReview(expandedView)
        return builder.build()
    }

    private fun setupUI(builder: NotificationCompat.Builder) {
        builder.setLargeIcon(getBitmap(baseNotificationModel.media?.fallbackUrl))
        val expandedBitmap: Bitmap = getBitmap(baseNotificationModel.media?.highQuality)
        expandedView.setImageViewBitmap(R.id.push_large_image, expandedBitmap)
        collapsedView.setTextViewText(R.id.push_title, getSpannedTextFromStr(baseNotificationModel.title))
        expandedView.setTextViewText(R.id.push_title, getSpannedTextFromStr(baseNotificationModel.title))
        expandedView.setTextViewText(R.id.push_message, getSpannedTextFromStr(baseNotificationModel.detailMessage))
    }

    private fun handleReview(remoteView: RemoteViews) {
        handleReviewStarClick(context, remoteView, ONE_STAR, R.id.ivStarReview_1)
        handleReviewStarClick(context, remoteView, TWO_STAR, R.id.ivStarReview_2)
        handleReviewStarClick(context, remoteView, THREE_STAR, R.id.ivStarReview_3)
        handleReviewStarClick(context, remoteView, FOUR_STAR, R.id.ivStarReview_4)
        handleReviewStarClick(context, remoteView, FIVE_STAR, R.id.ivStarReview_5)
    }

    private fun handleReviewStarClick(
        context: Context,
        remoteView: RemoteViews,
        starNumber: String,
        viewId: Int
    ) {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = ACTION_REVIEW_NOTIFICATION_STAR_CLICKED
        intent.putExtra(STAR_NUMBER, starNumber)
        remoteView.setOnClickPendingIntent(viewId,
            getPendingIntent(context, intent, requestCode))
    }

}