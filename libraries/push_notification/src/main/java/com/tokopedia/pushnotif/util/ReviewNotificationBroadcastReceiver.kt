package com.tokopedia.pushnotif.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.pushnotif.factory.ReviewNotificationFactory

class ReviewNotificationBroadcastReceiver : BroadcastReceiver() {
    companion object {
        private const val REVIEW_CLICK_AT = "REVIEW_CLICK_AT"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        var reviewPosition = 0
        when (intent?.action) {
            "5" -> reviewPosition = 5
            "4" -> reviewPosition = 4
            "3" -> reviewPosition = 3
            "2" -> reviewPosition = 2
            "1" -> reviewPosition = 1
        }

        context?.let {
            val createReviewRoute = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CREATE_REVIEW, "123", "123")
            createReviewRoute.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            createReviewRoute.putExtra(REVIEW_CLICK_AT, reviewPosition)

            val createReviewPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    createReviewRoute,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

            ReviewNotificationFactory(context).updateReviewNotification(reviewPosition)

            Handler().postDelayed({
                val closeNotificationBarIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                context.sendBroadcast(closeNotificationBarIntent)
                createReviewPendingIntent.send()
            }, 500)

        }
    }

}