package com.tokopedia.developer_options.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.tokopedia.developer_options.R
import com.tokopedia.pushnotif.ApplinkNotificationHelper
import com.tokopedia.pushnotif.Constant


object ReviewNotificationExample {

    lateinit var notificationLayout: RemoteViews

    @JvmStatic
    fun createReviewNotification(context: Context): Notification {
        val notifReview = NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL)
        notifReview.setSmallIcon(R.mipmap.ic_statusbar_notif_customer)
        notifReview.setCustomBigContentView(setupRemoteLayout(context))
        notifReview.setCustomContentView(setupSimpleRemoteLayout(context))
        notifReview.setStyle(NotificationCompat.DecoratedCustomViewStyle())
        notifReview.priority = NotificationCompat.PRIORITY_MAX
        if (ApplinkNotificationHelper.allowGroup()) {
            notifReview.setGroupSummary(true)
            notifReview.setGroup("reviews")
            notifReview.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
        }
        return notifReview.build()
    }

    private fun setupSimpleRemoteLayout(context: Context): RemoteViews {
        val packageName = context.applicationContext.packageName

        val simpleRemoteView = RemoteViews(packageName, com.tokopedia.pushnotif.R.layout.notification_review_simple_layout)
        simpleRemoteView.setTextViewText(com.tokopedia.pushnotif.R.id.notificationTitle, "Hi Karin!")
        simpleRemoteView.setTextViewText(
                com.tokopedia.pushnotif.R.id.notificationText,
                "Sudah [nn] hari, nih. Suka dengan [product] ini? Yuk, beri ulasan dan bagikan pengalamanmu ke pembeli lain!"
        )
        return simpleRemoteView
    }

    private fun setupRemoteLayout(context: Context): RemoteViews {
        val packageName = context.applicationContext.packageName

        notificationLayout = RemoteViews(packageName, com.tokopedia.pushnotif.R.layout.notification_review_layout)
        val listOfStars = listOf(com.tokopedia.pushnotif.R.id.rate_1, com.tokopedia.pushnotif.R.id.rate_2, com.tokopedia.pushnotif.R.id.rate_3, com.tokopedia.pushnotif.R.id.rate_4, com.tokopedia.pushnotif.R.id.rate_5)
        listOfStars.forEachIndexed { index, starId ->
            val truePosition = index + 1
            val intent = Intent(context, ReviewNotificationUpdateExample::class.java)
            intent.action = "$truePosition"

            notificationLayout.setOnClickPendingIntent(starId,
                    PendingIntent.getBroadcast(
                            context,
                            0,
                            intent,
                            0
                    )
            )
        }

        return notificationLayout
    }

    fun updateStars(context: Context, reviewPosition: Int) {
        for (i in 1..5) {
            val id = context.resources.getIdentifier("rate_$i", "id", context.packageName)
            // Here you can use any resource for selected and unselected ratings
            if (i <= reviewPosition) {
                notificationLayout.setImageViewResource(id, R.drawable.ic_stars_active_xxl)
            } else {
                notificationLayout.setImageViewResource(id, R.drawable.ic_stars_disable_xxl)
            }
        }

    }
}