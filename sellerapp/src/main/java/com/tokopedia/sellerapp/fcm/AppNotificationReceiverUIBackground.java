package com.tokopedia.sellerapp.fcm;

import android.app.Application;
import android.os.Bundle;

import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.base.BaseAppNotificationReceiverUIBackground;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alvarisi on 1/18/17.
 */

public class AppNotificationReceiverUIBackground extends BaseAppNotificationReceiverUIBackground {
    public static final String DEFAULT_NOTIF_CODE_VALUE = "0";
    private static final int DEFAULT_CART_VALUE = 0;
    private RemoteConfig remoteConfig;
    private AppNotificationReceiver receiver;

    public AppNotificationReceiverUIBackground(Application application) {
        super(application);
        remoteConfig = new FirebaseRemoteConfigImpl(application);
        //Hack reflection error don't remove it if reflection is already delete
        receiver = new AppNotificationReceiver();
    }

    @Override
    public void notifyReceiverBackgroundMessage(Bundle data) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "AppNotificationReceiverUIBackground_Seller");
        messageMap.put("isSupported", String.valueOf(isSupportedApplinkNotification(data)));
        messageMap.put("isDedicated", String.valueOf(isDedicatedNotification(data)));
        ServerLogger.log(Priority.P2, "PUSH_NOTIF_UNUSED", messageMap);
    }

    private boolean isSupportedApplinkNotification(Bundle bundle) {
        String applink = bundle.getString(Constants.ARG_NOTIFICATION_APPLINK, "");
        DeepLinkDelegate deepLinkDelegate = DeepLinkHandlerActivity.getDelegateInstance();
        return deepLinkDelegate.supportsUri(applink);
    }
}
