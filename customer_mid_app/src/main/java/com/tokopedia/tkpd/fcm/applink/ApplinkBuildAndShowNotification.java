package com.tokopedia.tkpd.fcm.applink;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.gcm.notification.applink.ApplinkPushNotificationBuildAndShow;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;

/**
 * Created by alvarisi on 3/6/17.
 */

public class ApplinkBuildAndShowNotification {

    public static void showApplinkNotification(Context context, Bundle data) {

        ApplinkPushNotificationBuildAndShow buildAndShow = new ApplinkPushNotificationBuildAndShow(data);
        Intent intent = new Intent(context, DeeplinkHandlerActivity.class);
        buildAndShow.process(context, intent);
    }
}
