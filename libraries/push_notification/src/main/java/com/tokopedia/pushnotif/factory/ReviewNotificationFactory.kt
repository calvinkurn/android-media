package com.tokopedia.pushnotif.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.tokopedia.pushnotif.ApplinkNotificationHelper
import com.tokopedia.pushnotif.Constant
import com.tokopedia.pushnotif.R
import com.tokopedia.pushnotif.model.ApplinkNotificationModel

class ReviewNotificationFactory(context: Context) : BaseNotificationFactory(context) {

    private val packageName = context.applicationContext.packageName
    override fun createNotification(applinkNotificationModel: ApplinkNotificationModel, notifcationType: Int, notificationId: Int): Notification {
        val notifReview = NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL)
        notifReview.setContentTitle(applinkNotificationModel.title)
        notifReview.setContentText(applinkNotificationModel.summary)
        notifReview.setSmallIcon(drawableIcon)
        notifReview.setLargeIcon(getBitmap(applinkNotificationModel.thumbnail))
        notifReview.setCustomBigContentView(setupRemoteLayout(context))
        notifReview.setCustomContentView(setupSimpleRemoteLayout())
        notifReview.setStyle(NotificationCompat.DecoratedCustomViewStyle())
        if (ApplinkNotificationHelper.allowGroup()) {
            notifReview.setGroupSummary(true)
            notifReview.setGroup(generateGroupKey(applinkNotificationModel.applinks))
            notifReview.setGroupAlertBehavior(Notification.GROUP_ALERT_CHILDREN)
        }
        notifReview.setChannelId("review_channel")
        notifReview.setOngoing(true)
        if (isAllowBell!!) {
            notifReview.setSound(ringtoneUri)
            if (isAllowVibrate!!) notifReview.setVibrate(vibratePattern)
        }

        return notifReview.build()
    }

    private fun setupSimpleRemoteLayout(): RemoteViews {
        val simpleRemoteView = RemoteViews(packageName, R.layout.notification_review_simple_layout)
        simpleRemoteView.setTextViewText(R.id.notificationTitle, "Hi Karinaa!")
        simpleRemoteView.setTextViewText(
                R.id.notificationText,
                "Sudah [nn] hari, nih. Suka dengan [product] ini? Yuk, beri ulasan dan bagikan pengalamanmu ke pembeli lain!"
        )
        return simpleRemoteView
    }

    private fun setupRemoteLayout(context: Context): RemoteViews {
        val notificationLayout = RemoteViews(packageName, R.layout.notification_review_layout)
        val listOfStars = listOf(R.id.rate_1, R.id.rate_2, R.id.rate_3, R.id.rate_4, R.id.rate_5)

        listOfStars.forEachIndexed { index, starId ->
            val truePosition = index + 1

            //TODO change yehez
            val intent = Intent(context, ReviewNotificationFactory::class.java)
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

}