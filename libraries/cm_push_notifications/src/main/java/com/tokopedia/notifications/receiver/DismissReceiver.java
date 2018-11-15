package com.tokopedia.notifications.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.tokopedia.notifications.common.CMConstant;

/**
 * @author lalit.singh
 */
public class DismissReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra(CMConstant.EXTRA_NOTIFICATION_ID, 0);
        NotificationManagerCompat.from(context).cancel(notificationId);
    }
}
