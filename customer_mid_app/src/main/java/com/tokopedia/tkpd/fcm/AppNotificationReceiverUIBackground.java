package com.tokopedia.tkpd.fcm;

import android.app.Application;
import android.os.Bundle;

import com.tokopedia.core.gcm.base.BaseAppNotificationReceiverUIBackground;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by alvarisi on 1/17/17.
 */

public class AppNotificationReceiverUIBackground extends BaseAppNotificationReceiverUIBackground {
    private static final String DEFAULT_NOTIF_CODE_VALUE = "0";
    private static final int DEFAULT_CART_VALUE = 0;
    private static final int DEFAULT_RIDE_URL_SIZE = 1;
    private RemoteConfig remoteConfig;

    public AppNotificationReceiverUIBackground(Application application) {
        super(application);
        remoteConfig = new FirebaseRemoteConfigImpl(application);
    }

    @Override
    public void notifyReceiverBackgroundMessage(Bundle bundle) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "AppNotificationReceiverUIBackground");
        messageMap.put("bundle", bundle.toString());
        ServerLogger.log(Priority.P2, "PUSH_NOTIF_UNUSED", messageMap);
    }

}
