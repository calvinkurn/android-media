package com.tokopedia.core.gcm.base;

import android.os.Build;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.core.gcm.di.DaggerFcmServiceComponent;
import com.tokopedia.fcmcommon.FirebaseMessagingManager;
import com.tokopedia.fcmcommon.di.DaggerFcmComponent;
import com.tokopedia.fcmcommon.di.FcmComponent;
import com.tokopedia.fcmcommon.di.FcmModule;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * @author by alvarisi on 1/10/17.
 */

public abstract class BaseNotificationMessagingService extends FirebaseMessagingService {

    UserSessionInterface userSession;

    @Inject
    FirebaseMessagingManager fcmManager;

    @Override
    public void onCreate() {
        super.onCreate();
        FcmComponent fcmComponent = DaggerFcmComponent.builder()
                .fcmModule(new FcmModule(getApplicationContext()))
                .build();
        DaggerFcmServiceComponent.builder()
                .fcmComponent(fcmComponent)
                .build()
                .inject(this);
    }


    public BaseNotificationMessagingService() {
        initUseSession();
    }

    private void initUseSession() {
        try {
            userSession = new UserSession(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Bundle convertMap(RemoteMessage message) {
        Map<String, String> map = message.getData();
        Bundle bundle = new Bundle(map != null ? map.size() : 0);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
        }
        return bundle;
    }


    @Override
    public void onNewToken(String newToken) {
        fcmManager.onNewToken(newToken);
        try {
            Timber.w("P2#TOKEN_REFRESH#Notification New Token - " + newToken + " | "
                    + userSession.getUserId() + " | " + userSession.getAccessToken() + " | "
                    + Build.FINGERPRINT + " | " + Build.MANUFACTURER + " | "
                    + Build.BRAND + " | " + Build.DEVICE + " | " + Build.PRODUCT + " | " + Build.MODEL
                    + " | " + Build.TAGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fcmManager.clear();
    }
}