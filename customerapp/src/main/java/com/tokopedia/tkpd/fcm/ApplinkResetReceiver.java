package com.tokopedia.tkpd.fcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpd.fcm.applink.ApplinkBuildAndShowNotification;

/**
 * Created by alvarisi on 3/5/17.
 */

public class ApplinkResetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String category = intent.getStringExtra(Constants.EXTRA_APPLINK_CATEGORY);

        ApplinkBuildAndShowNotification applinkBuildAndShowNotification = new ApplinkBuildAndShowNotification(context);
        switch (category) {
            case Constants.ARG_NOTIFICATION_APPLINK_MESSAGE:
                applinkBuildAndShowNotification.showMessagePersonalizedNotification(false);
                break;
            case Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION:
                applinkBuildAndShowNotification.showDiscussionPersonalizedNotification(false);
                break;
        }
    }
}