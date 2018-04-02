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
        Log.i("PUSH NOTIF", "KEHAPUS NEEEH");
        int notificationType = intent.getIntExtra(Constant.EXTRA_NOTIFICATION_TYPE, 0);
        HistoryNotification.clearHistoryNotification(context, notificationType);
    }
}
