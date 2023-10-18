package com.tokopedia.pushnotif.receiver

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.URLUtil
import com.tokopedia.config.GlobalConfig
import com.tokopedia.pushnotif.R
import com.tokopedia.pushnotif.data.constant.Constant
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel
import com.tokopedia.pushnotif.domain.TrackPushNotificationUseCase
import com.tokopedia.pushnotif.util.NotificationTracker

class NotifierReceiverActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifierreceiver)
        setTrackerClicked()
        finish()
    }

    private fun setTrackerClicked() {
        val transactionId = intent.getStringExtra(TrackPushNotificationUseCase.PARAM_TRANSACTION_ID).orEmpty()
        val recipientId = intent.getStringExtra(TrackPushNotificationUseCase.PARAM_RECIPIENT_ID).orEmpty()
        val appLinkValue = intent.getStringExtra(Constant.EXTRA_APPLINK_VALUE)
        val notificationType = intent.getIntExtra(Constant.EXTRA_NOTIFICATION_TYPE, 0)
        val notificationId = intent.getIntExtra(Constant.EXTRA_NOTIFICATION_ID, 0)

        val applinkNotificationModel = ApplinkNotificationModel().apply {
            setTransactionId(transactionId)
            toUserId = recipientId
        }

        NotificationTracker
            .getInstance(this)
            .trackDeliveredNotification(
                applinkNotificationModel,
                TrackPushNotificationUseCase.STATUS_CLICKED
            )

        startActivity(this, appLinkValue, notificationType, notificationId)
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
