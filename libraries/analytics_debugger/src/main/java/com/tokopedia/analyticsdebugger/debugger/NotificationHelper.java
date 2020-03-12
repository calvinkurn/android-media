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
import com.tokopedia.analyticsdebugger.debugger.ui.activity.AnalyticsDebuggerActivity;
import com.tokopedia.analyticsdebugger.debugger.ui.activity.ApplinkDebuggerActivity;
import com.tokopedia.analyticsdebugger.debugger.ui.activity.FpmDebuggerActivity;

/**
 * @author okasurya on 6/28/18.
 */
class NotificationHelper {
    private static final int NOTIFICATION_ID = 89324;
    private static final String NOTIF_CHANNEL_ID = "DEBUGGING_TOOLS_CHANNEL";
    private static final String NOTIF_CHANNEL_NAME = "Debugging Tools";

    public static void show(Context context, AnalyticsLogData data) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        setNotificationChannel(notificationManager);

        Intent intent = AnalyticsDebuggerActivity.newInstance(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle().bigText(data.getData());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle("Open Analytics Debugger")
                .setContentText(data.getName())
                .setAutoCancel(true)
                .setShowWhen(true)
                .setSmallIcon(com.tokopedia.design.R.drawable.ic_search_icon)
                .setStyle(inboxStyle)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    public static void show(Context context, PerformanceLogModel data) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        setNotificationChannel(notificationManager);

        Intent intent = FpmDebuggerActivity.newInstance(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle().bigText(data.getData());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle("Open Performance Debugger")
                .setContentText(data.getTraceName())
                .setAutoCancel(true)
                .setShowWhen(true)
                .setSmallIcon(com.tokopedia.design.R.drawable.ic_search_icon)
                .setStyle(inboxStyle)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    public static void show(Context context, ApplinkLogModel data) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        setNotificationChannel(notificationManager);

        Intent intent = ApplinkDebuggerActivity.newInstance(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle().bigText(data.getData());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle("Open Applink Debugger")
                .setContentText(data.getApplink())
                .setAutoCancel(true)
                .setShowWhen(true)
                .setSmallIcon(com.tokopedia.design.R.drawable.ic_search_icon)
                .setStyle(inboxStyle)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public static void setNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(
                    NOTIF_CHANNEL_ID,
                    NOTIF_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            ));
        }
    }
}
