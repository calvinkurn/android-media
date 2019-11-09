package com.tokopedia.developer_options.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.core.app.NotificationManagerCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

class ReviewNotificationUpdateExample : BroadcastReceiver() {

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
            val pmSubscribeRoute = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CREATE_REVIEW, "252378957", "25522735")
            pmSubscribeRoute.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            pmSubscribeRoute.putExtra("REVIEW_CLICK_AT", reviewPosition)
            val pmSubscribeIntent = PendingIntent.getActivity(
                    context,
                    0,
                    pmSubscribeRoute,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notifReview = ReviewNotificationExample.createReviewNotification(context)
            ReviewNotificationExample.updateStars(context, reviewPosition)
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(777, notifReview)

            Handler().postDelayed({
                val closeNotificationBarIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                context.sendBroadcast(closeNotificationBarIntent)
                pmSubscribeIntent.send()
            }, 500)

        }
    }
}