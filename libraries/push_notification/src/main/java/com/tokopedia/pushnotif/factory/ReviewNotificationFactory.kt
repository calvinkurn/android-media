package com.tokopedia.pushnotif.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.pushnotif.ApplinkNotificationHelper
import com.tokopedia.pushnotif.data.constant.Constant
import com.tokopedia.pushnotif.R
import com.tokopedia.pushnotif.data.model.ReviewNotificationModel
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel
import com.tokopedia.pushnotif.services.ReviewNotificationBroadcastReceiver
import com.tokopedia.pushnotif.util.PendingIntentUtil
import java.util.concurrent.TimeUnit

class ReviewNotificationFactory(context: Context) : BaseNotificationFactory(context) {

    companion object {
        @JvmField
        val TAG: String = ReviewNotificationFactory::class.java.simpleName
    }

    private val packageName = context.applicationContext.packageName
    private val cacheManager: PersistentCacheManager = PersistentCacheManager(context)
    private var resultReviewModel: ReviewNotificationModel = ReviewNotificationModel()
    private lateinit var notificationLayout: RemoteViews


    val notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(context)
    }

    private val notificationBuilder = NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL).apply {
        setSmallIcon(drawableIcon)
        setStyle(NotificationCompat.DecoratedCustomViewStyle())
        priority = NotificationCompat.PRIORITY_MAX
        if (ApplinkNotificationHelper.allowGroup()) {
            setGroupSummary(true)
            setGroup("reviews")
            setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
        }
        if (isAllowBell!!) {
            setSound(ringtoneUri)
            if (isAllowVibrate!!) setVibrate(vibratePattern)
        }
    }

    override fun createNotification(applinkNotificationModel: ApplinkNotificationModel, notificationType: Int, notificationId: Int): Notification {
        storeToTransaction(context, notificationType, notificationId, applinkNotificationModel)

        cacheManager.delete(TAG)
        cacheManager.put(TAG, ReviewNotificationModel(
                applinkNotificationModel,
                notificationType,
                notificationId
        ), TimeUnit.DAYS.toMillis(7))

        resultReviewModel = cacheManager.get(TAG, ReviewNotificationModel::class.java) ?: ReviewNotificationModel()

        val title = if (resultReviewModel.applinkNotificationModel.title.isEmpty())
            context.resources.getString(R.string.title_general_push_notification)
        else resultReviewModel.applinkNotificationModel.title

        notificationBuilder
                .setContentIntent(
                        PendingIntentUtil.createPendingIntent(
                                context,
                                resultReviewModel.applinkNotificationModel.applinks,
                                resultReviewModel.notificationType,
                                resultReviewModel.notificationId)
                )
                .setCustomBigContentView(
                        setupRemoteLayout(
                                title,
                                resultReviewModel.applinkNotificationModel.desc,
                                resultReviewModel.applinkNotificationModel.thumbnail))
                .setCustomContentView(
                        setupSimpleRemoteLayout(
                                title,
                                resultReviewModel.applinkNotificationModel.desc,
                                resultReviewModel.applinkNotificationModel.thumbnail))

        notificationManager.notify(resultReviewModel.notificationId, notificationBuilder.build())

        return Notification()
    }

    fun updateReviewNotification(reviewPosition: Int) {
        resultReviewModel = cacheManager.get(TAG, ReviewNotificationModel::class.java)
                ?: ReviewNotificationModel()
        loadImageBitmap(resultReviewModel.applinkNotificationModel.thumbnail, reviewPosition)
    }

    // This function is use for load review for the first time, (getBitmap should run on another thread)
    private fun setupSimpleRemoteLayout(title: String, summary: String, imageUrl: String): RemoteViews {
        val simpleRemoteView = RemoteViews(packageName, R.layout.notification_review_simple_layout)
        simpleRemoteView.setTextViewText(R.id.notificationTitle, title)
        simpleRemoteView.setTextViewText(
                R.id.notificationText,
                summary
        )

        simpleRemoteView.setImageViewBitmap(R.id.notificationImage, getBitmap(imageUrl))
        return simpleRemoteView
    }

    // This function is use for load review after updated, or from BroadcastReceiver
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

    // This function is use for load review for the first time, (getBitmap should run on another thread)
    private fun setupRemoteLayout(title: String, desc: String, imageUrl: String): RemoteViews {
        val intent = Intent(context, ReviewNotificationBroadcastReceiver::class.java)

        notificationLayout = RemoteViews(packageName, R.layout.notification_review_layout)
        notificationLayout.setTextViewText(R.id.rate_title, title)
        notificationLayout.setTextViewText(R.id.rate_message, desc)
        notificationLayout.setImageViewBitmap(R.id.img_notif, getBitmap(imageUrl))
        notificationLayout.setOnClickPendingIntent(
                R.id.notif_review_container,
                PendingIntentUtil.createPendingIntent(
                        context,
                        resultReviewModel.applinkNotificationModel.applinks,
                        resultReviewModel.notificationType,
                        resultReviewModel.notificationId)
        )

        val listOfStars = listOf(R.id.rate_1, R.id.rate_2, R.id.rate_3, R.id.rate_4, R.id.rate_5)
        listOfStars.forEachIndexed { index, starId ->
            val truePosition = index + 1

            intent.action = "$truePosition"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                notificationLayout.setOnClickPendingIntent(starId,
                        PendingIntent.getBroadcast(
                                context,
                                0,
                                intent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                        )
                )
            }else{
                notificationLayout.setOnClickPendingIntent(starId,
                    PendingIntent.getBroadcast(
                        context,
                        0,
                        intent,
                        0
                    )
                )
            }
        }
        return notificationLayout
    }

    private fun updateStars(context: Context, reviewPosition: Int) {
        for (i in 1..5) {
            val id = context.resources.getIdentifier("rate_$i", "id", context.packageName)
            // Here you can use any resource for selected and unselected ratings
            if (i <= reviewPosition) {
                notificationLayout.setImageViewResource(id, com.tokopedia.design.R.drawable.ic_stars_active_xxl)
            } else {
                notificationLayout.setImageViewResource(id, com.tokopedia.design.R.drawable.ic_stars_disable_xxl)
            }
        }
    }

    private fun loadImageBitmap(imgUrl: String, reviewPosition: Int) {
        Handler(Looper.getMainLooper()).post {
            Glide.with(context.applicationContext)
                    .asBitmap()
                    .load(imgUrl)
                    .error(com.tokopedia.resources.common.R.drawable.ic_big_notif_customerapp)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) { }

                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
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
