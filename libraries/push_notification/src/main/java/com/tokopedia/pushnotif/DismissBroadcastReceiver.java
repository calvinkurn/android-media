package com.tokopedia.pushnotif;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author ricoharisin .
 */

public class DismissBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationType = intent.getIntExtra(Constant.EXTRA_NOTIFICATION_TYPE, 0);
        int notificationId = intent.getIntExtra(Constant.EXTRA_NOTIFICATION_ID, 0);

        if (notificationId == 0) {
            HistoryNotification.clearAllHistoryNotification(context, notificationType);
        } else {
            HistoryNotification.clearHistoryNotification(context, notificationType, notificationId);
        }
    }
}
