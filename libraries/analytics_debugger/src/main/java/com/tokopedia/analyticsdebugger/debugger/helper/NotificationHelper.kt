package com.tokopedia.analyticsdebugger.debugger.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

import androidx.core.app.NotificationCompat
import com.tokopedia.analyticsdebugger.cassava.debugger.AnalyticsDebuggerActivity

import com.tokopedia.analyticsdebugger.debugger.domain.model.AnalyticsLogData
import com.tokopedia.analyticsdebugger.debugger.domain.model.ApplinkLogModel
import com.tokopedia.analyticsdebugger.debugger.domain.model.PerformanceLogModel
import com.tokopedia.analyticsdebugger.debugger.domain.model.TopAdsLogModel
import com.tokopedia.analyticsdebugger.debugger.ui.activity.ApplinkDebuggerActivity
import com.tokopedia.analyticsdebugger.debugger.ui.activity.FpmDebuggerActivity
import com.tokopedia.analyticsdebugger.debugger.ui.activity.TopAdsDebuggerActivity

/**
 * @author okasurya on 6/28/18.
 */
internal object NotificationHelper {
    private val NOTIF_ID_ANALYTICS_DEBUGGER = 89324
    private val NOTIF_ID_PERFORMANCE_DEBUGGER = 89325
    private val NOTIF_ID_APPLINK_DEBUGGER = 89326
    private val NOTIF_ID_TOPADS_DEBUGGER = 89327

    private val NOTIF_TITLE_ANALYTICS_DEBUGGER = "Open Analytics Debugger"
    private val NOTIF_TITLE_PERFORMANCE_DEBUGGER = "Open Performance Debugger"
    private val NOTIF_TITLE_APPLINK_DEBUGGER = "Open Applink Debugger"
    private val NOTIF_TITLE_TOPADS_DEBUGGER = "Open TopAds Debugger"

    private val NOTIF_CHANNEL_ID = "DEBUGGING_TOOLS_CHANNEL"
    private val NOTIF_CHANNEL_NAME = "Debugging Tools"

    fun show(context: Context, data: AnalyticsLogData) {
        showNotif(context, NOTIF_ID_ANALYTICS_DEBUGGER, NOTIF_TITLE_ANALYTICS_DEBUGGER,
                data.name, data.data, AnalyticsDebuggerActivity.newInstance(context))
    }

    fun show(context: Context, data: PerformanceLogModel) {
        showNotif(context, NOTIF_ID_PERFORMANCE_DEBUGGER, NOTIF_TITLE_PERFORMANCE_DEBUGGER,
                data.traceName, data.data, FpmDebuggerActivity.newInstance(context))
    }

    fun show(context: Context, data: ApplinkLogModel) {
        showNotif(context, NOTIF_ID_APPLINK_DEBUGGER, NOTIF_TITLE_APPLINK_DEBUGGER,
                data.applink, data.data, ApplinkDebuggerActivity.newInstance(context))
    }

    fun show(context: Context, data: TopAdsLogModel) {
        showNotif(context, NOTIF_ID_TOPADS_DEBUGGER, NOTIF_TITLE_TOPADS_DEBUGGER,
                data.sourceName, data.data, TopAdsDebuggerActivity.newInstance(context))
    }

    private fun showNotif(context: Context, notifId: Int, contentTitle: String,
                          contentText: String?, payload: String?, intent: Intent) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        setNotificationChannel(notificationManager)

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val inboxStyle = NotificationCompat.BigTextStyle().bigText(payload)
        val builder = NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setSmallIcon(com.tokopedia.design.R.drawable.ic_search_icon)
                .setStyle(inboxStyle)
                .setPriority(NotificationCompat.PRIORITY_LOW)
        notificationManager.notify(notifId, builder.build())
    }

    private fun setNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel(
                    NOTIF_CHANNEL_ID,
                    NOTIF_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            ))
        }
    }
}
