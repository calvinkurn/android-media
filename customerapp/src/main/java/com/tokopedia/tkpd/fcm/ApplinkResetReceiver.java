package com.tokopedia.tkpd.fcm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;

import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpd.deeplink.DeepLinkReceiver;

/**
 * Created by alvarisi on 3/5/17.
 */

public class ApplinkResetReceiver extends BroadcastReceiver {
    private static final String TAG = DeepLinkReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String category = intent.getStringExtra(Constants.EXTRA_APPLINK_CATEGORY);

        ApplinkBuildAndShowNotification applinkBuildAndShowNotification = new ApplinkBuildAndShowNotification(context);
        switch (category) {
            case Constants.ARG_NOTIFICATION_APPLINK_MESSAGE:
                applinkBuildAndShowNotification.resetIfActiveNotificationMessage();
                break;
            case Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION:
                applinkBuildAndShowNotification.resetIfActiveNotificationDiscussion();
                break;
        }
    }
}