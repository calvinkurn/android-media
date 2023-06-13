package com.tokopedia.pushnotif.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.URLUtil
import com.tokopedia.config.GlobalConfig
import com.tokopedia.pushnotif.data.constant.Constant

/**
 * @author by milhamj on 2019-12-06.
 */

object PendingIntentUtil {
    fun createPendingIntent(context: Context, appLinks: String,
                            notificationType: Int, notificationId: Int): PendingIntent {
        val resultPendingIntent: PendingIntent
        val intent = Intent()
        // Notification will go through DeeplinkActivity and DeeplinkHandlerActivity
        // because we need tracking UTM for those notification applink
        if (URLUtil.isNetworkUrl(appLinks)) {
            intent.setClassName(context.getPackageName(), GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME)
        } else {
            intent.setClassName(context.getPackageName(), GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME)
        }
        intent.data = Uri.parse(appLinks)
        val bundle = Bundle()
        bundle.putBoolean(Constant.EXTRA_APPLINK_FROM_PUSH, true)
        bundle.putInt(Constant.EXTRA_NOTIFICATION_TYPE, notificationType)
        bundle.putInt(Constant.EXTRA_NOTIFICATION_ID, notificationId)
        intent.putExtras(bundle)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            resultPendingIntent = PendingIntent.getActivity(
                    context,
                    notificationId,
                    intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        }

        return resultPendingIntent
    }
}
