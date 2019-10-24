package com.tokopedia.pushnotif.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.pushnotif.ApplinkNotificationHelper
import com.tokopedia.pushnotif.Constant
import com.tokopedia.pushnotif.R
import com.tokopedia.pushnotif.db.model.ReviewNotificationModel
import com.tokopedia.pushnotif.model.ApplinkNotificationModel
import com.tokopedia.pushnotif.util.ReviewNotificationBroadcastReceiver
import java.util.concurrent.TimeUnit


class ReviewNotificationFactory(context: Context) : BaseNotificationFactory(context) {

    private val cacheManager = PersistentCacheManager(context)
    lateinit var notificationLayout: RemoteViews
    var resultReviewModel: ReviewNotificationModel = cacheManager.get(TAG, ReviewNotificationModel::class.java)
            ?: ReviewNotificationModel()
    val notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(context)
    }

    companion object {
        @JvmField
        val TAG: String = ReviewNotificationFactory::class.java.simpleName
    }

    private val notificationBuilder = NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL).apply {
        setSmallIcon(drawableIcon)
        setStyle(NotificationCompat.DecoratedCustomViewStyle())
        priority = NotificationCompat.PRIORITY_MAX
        if (ApplinkNotificationHelper.allowGroup()) {
            setGroupSummary(true)
            setGroup("reviews")
            setGroupAlertBehavior(Notification.GROUP_ALERT_CHILDREN)
        }
        if (isAllowBell!!) {
            setSound(ringtoneUri)
            if (isAllowVibrate!!) setVibrate(vibratePattern)
        }
    }

    private val packageName = context.applicationContext.packageName
    override fun createNotification(applinkNotificationModel: ApplinkNotificationModel, notifcationType: Int, notificationId: Int): Notification {
        //NO OP
        return Notification()
    }

    fun createNotificationCustom(applinkNotificationModel: ApplinkNotificationModel, notifcationType: Int, notificationId: Int) {
        cacheManager.put(TAG, ReviewNotificationModel(applinkNotificationModel, notifcationType, notificationId), TimeUnit.DAYS.toMillis(7))
        notificationBuilder.setCustomBigContentView(setupRemoteLayout(resultReviewModel.applinkNotificationModel.title, resultReviewModel.applinkNotificationModel.desc))
                .setCustomContentView(setupSimpleRemoteLayout(resultReviewModel.applinkNotificationModel.title, resultReviewModel.applinkNotificationModel.desc))
        notificationManager.notify(resultReviewModel.notificationId, notificationBuilder.build())
    }

    fun updateReviewNotification(reviewPosition: Int) {
        loadImageBitmap("https://ecs7.tokopedia.net/img/attachment/2019/8/20/22796090/22796090_b9954493-b3f2-4abe-b225-8685a5f71135.jpg", reviewPosition)
    }

    private fun setupSimpleRemoteLayout(title: String, summary: String): RemoteViews {
        val simpleRemoteView = RemoteViews(packageName, R.layout.notification_review_simple_layout)
        simpleRemoteView.setTextViewText(R.id.notificationTitle, title)
        simpleRemoteView.setTextViewText(
                R.id.notificationText,
                summary
        )

        simpleRemoteView.setImageViewBitmap(R.id.notificationImage, getBitmap("https://ecs7.tokopedia.net/img/attachment/2019/8/20/22796090/22796090_b9954493-b3f2-4abe-b225-8685a5f71135.jpg"))
        return simpleRemoteView
    }

    private fun setupSimpleRemoteWithHandler(title: String, summary: String, imageBitmap: Bitmap): RemoteViews {
        val simpleRemoteView = RemoteViews(packageName, R.layout.notification_review_simple_layout)
        simpleRemoteView.setTextViewText(R.id.notificationTitle, title)
        simpleRemoteView.setTextViewText(
                R.id.notificationText,
                summary
        )

        simpleRemoteView.setImageViewBitmap(R.id.notificationImage, imageBitmap)
        return simpleRemoteView
    }

    private fun setupBigRemoteWithHandler(imgBitmap: Bitmap): RemoteViews {
        notificationLayout = RemoteViews(packageName, R.layout.notification_review_layout)
        notificationLayout.setTextViewText(R.id.rate_title, resultReviewModel.applinkNotificationModel.title)
        notificationLayout.setTextViewText(R.id.rate_message, resultReviewModel.applinkNotificationModel.desc)
        notificationLayout.setImageViewBitmap(R.id.img_notif, imgBitmap)
        return notificationLayout
    }

    private fun setupRemoteLayout(title: String, desc: String): RemoteViews {
        notificationLayout = RemoteViews(packageName, R.layout.notification_review_layout)
        notificationLayout.setTextViewText(R.id.rate_title, title)
        notificationLayout.setTextViewText(R.id.rate_message, desc)
        notificationLayout.setImageViewBitmap(R.id.img_notif, getBitmap("https://ecs7.tokopedia.net/img/attachment/2019/8/20/22796090/22796090_b9954493-b3f2-4abe-b225-8685a5f71135.jpg"))

        val listOfStars = listOf(R.id.rate_1, R.id.rate_2, R.id.rate_3, R.id.rate_4, R.id.rate_5)
        listOfStars.forEachIndexed { index, starId ->
            val truePosition = index + 1
            val intent = Intent(context, ReviewNotificationBroadcastReceiver::class.java)
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

    fun loadImageBitmap(imgUrl: String, reviewPosition: Int) {
        Handler(Looper.getMainLooper()).post {
            Glide.with(context.applicationContext)
                    .load(imgUrl)
                    .asBitmap()
                    .error(R.drawable.ic_big_notif_customerapp)
                    .into(object : SimpleTarget<Bitmap>(100, 100) {
                        override fun onResourceReady(resource: Bitmap,
                                                     glideAnimation: GlideAnimation<in Bitmap>?) {
                            notificationBuilder
                                    .setCustomContentView(setupSimpleRemoteWithHandler(resultReviewModel.applinkNotificationModel.title, resultReviewModel.applinkNotificationModel.desc, resource))
                                    .setCustomBigContentView(setupBigRemoteWithHandler(resource))
                            updateStars(context, reviewPosition)
                            notificationManager.notify(resultReviewModel.notificationId, notificationBuilder.build())
                        }
                    })
        }
    }

}