package com.tokopedia.tkpd.fcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.gcm.Constants;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.tkpd.fcm.applink.ApplinkBuildAndShowNotification;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by alvarisi on 3/5/17.
 */

public class ApplinkResetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String category = intent.getStringExtra(Constants.EXTRA_APPLINK_CATEGORY);
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "ApplinkResetReceiver");
        messageMap.put("category", category);
        ServerLogger.INSTANCE.log(Priority.P2, "PUSH_NOTIF_UNUSED", messageMap);
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