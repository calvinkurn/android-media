package com.tokopedia.analyticsdebugger.debugger;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.tokopedia.analyticsdebugger.debugger.domain.model.AnalyticsLogData;
import com.tokopedia.analyticsdebugger.debugger.domain.model.ApplinkLogModel;
import com.tokopedia.analyticsdebugger.debugger.domain.model.PerformanceLogModel;
import com.tokopedia.analyticsdebugger.debugger.ui.activity.ApplinkDebuggerActivity;

/**
 * @author okasurya on 6/28/18.
 */
class NotificationHelper {
    private static final int NOTIF_ID_ANALYTICS_DEBUGGER = 89324;
    private static final int NOTIF_ID_PERFORMANCE_DEBUGGER = 89325;
    private static final int NOTIF_ID_APPLINK_DEBUGGER = 89326;

    private static final String NOTIF_TITLE_ANALYTICS_DEBUGGER = "Open Analytics Debugger";
    private static final String NOTIF_TITLE_PERFORMANCE_DEBUGGER = "Open Performance Debugger";
    private static final String NOTIF_TITLE_APPLINK_DEBUGGER = "Open Applink Debugger";

    private static final String NOTIF_CHANNEL_ID = "DEBUGGING_TOOLS_CHANNEL";
    private static final String NOTIF_CHANNEL_NAME = "Debugging Tools";

    public static void show(Context context, AnalyticsLogData data) {
        showNotif(context, NOTIF_ID_ANALYTICS_DEBUGGER, NOTIF_TITLE_ANALYTICS_DEBUGGER, data.getName(), data.getData());
    }

    public static void show(Context context, PerformanceLogModel data) {
        showNotif(context, NOTIF_ID_PERFORMANCE_DEBUGGER, NOTIF_TITLE_PERFORMANCE_DEBUGGER, data.getTraceName(), data.getData());
    }

    public static void show(Context context, ApplinkLogModel data) {
        showNotif(context, NOTIF_ID_APPLINK_DEBUGGER, NOTIF_TITLE_APPLINK_DEBUGGER, data.getApplink(), data.getData());
    }

    private static void showNotif(Context context, int notifId, String contentTitle, String contentText, String payload) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        setNotificationChannel(notificationManager);

        Intent intent = ApplinkDebuggerActivity.newInstance(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle().bigText(payload);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setSmallIcon(com.tokopedia.design.R.drawable.ic_search_icon)
                .setStyle(inboxStyle)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        notificationManager.notify(notifId, builder.build());
    }

    private static void setNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(
                    NOTIF_CHANNEL_ID,
                    NOTIF_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            ));
        }
    }
}
