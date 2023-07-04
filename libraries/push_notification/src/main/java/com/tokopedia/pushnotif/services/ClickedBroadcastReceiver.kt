package com.tokopedia.pushnotif.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.URLUtil
import com.tokopedia.config.GlobalConfig
import com.tokopedia.pushnotif.data.constant.Constant
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel
import com.tokopedia.pushnotif.domain.TrackPushNotificationUseCase
import com.tokopedia.pushnotif.util.NotificationTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ClickedBroadcastReceiver : BroadcastReceiver(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun onReceive(context: Context, intent: Intent) {
        launch {
            try {
                val notificationType = intent.getIntExtra(Constant.EXTRA_NOTIFICATION_TYPE, 0)
                val notificationId = intent.getIntExtra(Constant.EXTRA_NOTIFICATION_ID, 0)
                val appLinkValue = intent.getStringExtra(Constant.EXTRA_APPLINK_VALUE)
                val transactionId = intent.getStringExtra(TrackPushNotificationUseCase.PARAM_TRANSACTION_ID).orEmpty()
                val recipientId = intent.getStringExtra(TrackPushNotificationUseCase.PARAM_RECIPIENT_ID).orEmpty()

                val applinkNotificationModel = ApplinkNotificationModel().apply {
                    setTransactionId(transactionId)
                    toUserId = recipientId
                }

                NotificationTracker
                    .getInstance(context)
                    .trackDeliveredNotification(
                        applinkNotificationModel,
                        TrackPushNotificationUseCase.STATUS_CLICKED
                    )

                startActivity(context, appLinkValue, notificationType, notificationId)
            } catch (ignored: Exception) {

            }
        }
    }

    private fun startActivity(
        context: Context,
        appLink: String?,
        notificationType: Int,
        notificationId: Int,
    ) {
        val appLinkIntent = Intent()

        // Notification will go through DeeplinkActivity and DeeplinkHandlerActivity
        // because we need tracking UTM for those notification applink

        // Notification will go through DeeplinkActivity and DeeplinkHandlerActivity
        // because we need tracking UTM for those notification applink
        if (URLUtil.isNetworkUrl(appLink)) {
            appLinkIntent.setClassName(
                context.packageName,
                GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME
            )
        } else {
            appLinkIntent.setClassName(
                context.packageName,
                GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME
            )
        }

        appLinkIntent.data = Uri.parse(appLink)
        val bundle = Bundle()
        bundle.putBoolean(Constant.EXTRA_APPLINK_FROM_PUSH, true)
        bundle.putInt(Constant.EXTRA_NOTIFICATION_TYPE, notificationType)
        bundle.putInt(Constant.EXTRA_NOTIFICATION_ID, notificationId)
        appLinkIntent.putExtras(bundle)

        appLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.applicationContext.startActivity(appLinkIntent)
    }

}
