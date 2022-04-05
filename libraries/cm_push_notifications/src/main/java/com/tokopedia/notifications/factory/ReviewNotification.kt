package com.tokopedia.notifications.factory

import android.app.Notification
import android.content.Context
import android.content.Intent
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

    private val headsUpView by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            RemoteViews(packageName, R.layout.cm_layout_headsup_review_collapsed)
        else RemoteViews(packageName, R.layout.cm_layout_headsup_review_collapsed_pre_dark_mode)
    }

    private val expandedView by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            RemoteViews(packageName, R.layout.cm_layout_review_expand)
        else RemoteViews(packageName, R.layout.cm_layout_review_expand_pre_dark_mode)
    }

    override fun createNotification(): Notification? {
        val builder = builder
        setupUI(builder)
        setUpHeadsUpRemoteView()

        builder.setLargeIcon(null)
            .setCustomHeadsUpContentView(headsUpView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setContentIntent(createMainPendingIntent(baseNotificationModel, requestCode))
            .setDeleteIntent(
                createDismissPendingIntent(
                    baseNotificationModel.notificationId,
                    requestCode
                )
            )
        handleReview(headsUpView)
        handleReview(collapsedView)
        handleReview(expandedView)
        return builder.build()
    }

    private fun setupUI(builder: NotificationCompat.Builder) {
        builder.setLargeIcon(getBitmap(baseNotificationModel.media?.fallbackUrl))
        val expandedBitmap: Bitmap = getBitmap(baseNotificationModel.media?.highQuality)
        expandedView.setImageViewBitmap(R.id.ivProductLargeImage, expandedBitmap)
        collapsedView.setImageViewBitmap(R.id.ivProductIcon, expandedBitmap)
        collapsedView.setTextViewText(
            R.id.tvReviewPushTitle,
            getSpannedTextFromStr(baseNotificationModel.title)
        )
        expandedView.setTextViewText(
            R.id.tvReviewPushTitle,
            getSpannedTextFromStr(baseNotificationModel.title)
        )
        expandedView.setTextViewText(
            R.id.tvReviewPushMessage,
            getSpannedTextFromStr(baseNotificationModel.detailMessage?:baseNotificationModel.message)
        )
    }

    /**
     * Remote View for Collapsed Mode and will be displayed on Screen as Heads up view not in Notification Tray
     **/
    private fun setUpHeadsUpRemoteView() {
        val expandedBitmap: Bitmap = getBitmap(baseNotificationModel.media?.highQuality)
        headsUpView.setImageViewBitmap(R.id.ivProductIcon, expandedBitmap)
        headsUpView.setTextViewText(
            R.id.tvReviewPushTitle,
            getSpannedTextFromStr(baseNotificationModel.title)
        )
        headsUpView.setOnClickPendingIntent(
            R.id.push_noti_background,
            createMainPendingIntent(baseNotificationModel, requestCode)
        )
    }

    private fun handleReview(remoteView: RemoteViews) {
        setStarClickPendingIntent(context, remoteView, ONE_STAR, R.id.ivReviewStarOne)
        setStarClickPendingIntent(context, remoteView, TWO_STAR, R.id.ivReviewStarTwo)
        setStarClickPendingIntent(context, remoteView, THREE_STAR, R.id.ivReviewStarThree)
        setStarClickPendingIntent(context, remoteView, FOUR_STAR, R.id.ivReviewStarFour)
        setStarClickPendingIntent(context, remoteView, FIVE_STAR, R.id.ivReviewStarFive)
    }

    private fun setStarClickPendingIntent(
        context: Context,
        remoteView: RemoteViews,
        starNumber: String,
        viewId: Int
    ) {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = ACTION_REVIEW_NOTIFICATION_STAR_CLICKED
        intent.putExtra(STAR_NUMBER, starNumber)
        remoteView.setOnClickPendingIntent(
            viewId,
            getPendingIntent(context, intent, requestCode)
        )
    }

    companion object {
        private const val reviewRegex = "={{1-5}}"

        fun updateReviewAppLink(
            intent: Intent,
            baseNotificationModel: BaseNotificationModel?
        ): BaseNotificationModel? {
            val starNumber = intent.getStringExtra(STAR_NUMBER)
            val appLink = starNumber?.let {
                baseNotificationModel?.appLink?.replace(reviewRegex, "=$starNumber") ?: ""
            }
            baseNotificationModel?.appLink = appLink ?: ""
            return baseNotificationModel
        }
    }

}