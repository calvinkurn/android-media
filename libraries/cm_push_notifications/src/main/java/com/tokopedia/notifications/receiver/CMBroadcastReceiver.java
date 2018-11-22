package com.tokopedia.notifications.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.notifications.common.CMConstant;

/**
 * @author lalit.singh
 */
public class CMBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int notificationId = intent.getIntExtra(CMConstant.EXTRA_NOTIFICATION_ID, 0);
        if (null != action) {
            switch (action) {
                case CMConstant.ReceiverAction.ACTION_BUTTON:
                    String appLinks = intent.getStringExtra(CMConstant.ActionButtonExtra.ACTION_BUTTON_APP_LINK);
                    Intent appLinkIntent = RouteManager.getIntent(context, appLinks);
                    context.startActivity(appLinkIntent);
                    NotificationManagerCompat.from(context).cancel(notificationId);
                    break;
                case CMConstant.ReceiverAction.ACTION_CANCEL_PERSISTENT:
                    NotificationManagerCompat.from(context).cancel(notificationId);
                    break;
                case CMConstant.ReceiverAction.ACTION_ON_NOTIFICATION_DISMISS:
                    NotificationManagerCompat.from(context).cancel(notificationId);
                    break;
            }
        }
    }

}
