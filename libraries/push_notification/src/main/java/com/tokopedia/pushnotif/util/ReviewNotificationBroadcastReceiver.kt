package com.tokopedia.pushnotif.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.pushnotif.db.model.ReviewNotificationModel
import com.tokopedia.pushnotif.factory.ReviewNotificationFactory

class ReviewNotificationBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val REVIEW_CLICK_AT = "REVIEW_CLICK_AT"
        const val REVIEW_NOTIFICATION_ID = "REVIEW_NOTIFICATION_ID"
        const val REVIEW_APPLINK_EXTRA = "REVIEW_APPLINK_EXTRA"
        const val DEFAULT_STAR = 5
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val reviewPosition = intent?.action?.toIntOrNull() ?: DEFAULT_STAR

        context?.let {
            val cacheManager = PersistentCacheManager(it)
            val reviewModel = cacheManager.get(ReviewNotificationFactory.TAG,
                    ReviewNotificationModel::class.java)
                    ?: ReviewNotificationModel()
            val applink = reviewModel.getApplinkWithRating(reviewPosition.toString())

            val createReviewRoute = RouteManager.getIntent(context, applink)
            createReviewRoute.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            createReviewRoute.putExtra(REVIEW_CLICK_AT, reviewPosition)
            createReviewRoute.putExtra(REVIEW_NOTIFICATION_ID, intent?.getIntExtra(REVIEW_NOTIFICATION_ID, 0))

            val createReviewPendingIntent = PendingIntentUtil.createPendingIntent(it, applink, reviewModel.notificationType, reviewModel.notificationId)

            ReviewNotificationFactory(context).updateReviewNotification(reviewPosition)

            Handler().postDelayed({
                closeNotificationBar(it)
                createReviewPendingIntent.send()
            }, 500)

        }
    }


    private fun closeNotificationBar(context: Context) {
        val closeNotificationBarIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(closeNotificationBarIntent)
    }
}