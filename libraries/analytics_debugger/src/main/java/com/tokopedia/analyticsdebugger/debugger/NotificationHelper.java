package com.tokopedia.analyticsdebugger.debugger;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.tokopedia.analyticsdebugger.debugger.domain.model.AnalyticsLogData;
import com.tokopedia.analyticsdebugger.debugger.ui.activity.AnalyticsDebuggerActivity;

/**
 * @author okasurya on 6/28/18.
 */
class NotificationHelper {
    private static final int NOTIFICATION_ID = 89324;

    public static void show(Context context, AnalyticsLogData data) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = AnalyticsDebuggerActivity.newInstance(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle().bigText(data.getData());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ANDROID_GENERAL_CHANNEL")
                .setContentIntent(pendingIntent)
                .setContentTitle("Open Analytics Debugger")
                .setContentText(data.getName())
                .setAutoCancel(true)
                .setShowWhen(true)
                .setSmallIcon(com.tokopedia.design.R.drawable.ic_search_icon)
                .setStyle(inboxStyle);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}
