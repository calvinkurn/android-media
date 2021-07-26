package com.tokopedia.core.gcm.base;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.FCMTokenReceiver;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.IFCMTokenReceiver;
import com.tokopedia.core.gcm.di.DaggerFcmServiceComponent;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;
import com.tokopedia.fcmcommon.FirebaseMessagingManager;
import com.tokopedia.fcmcommon.di.DaggerFcmComponent;
import com.tokopedia.fcmcommon.di.FcmComponent;
import com.tokopedia.fcmcommon.di.FcmModule;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.moengage_wrapper.MoengageInteractor;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.hansel.hanselsdk.Hansel;
import rx.Observable;
import timber.log.Timber;

/**
 * @author by alvarisi on 1/10/17.
 */

public abstract class BaseNotificationMessagingService extends FirebaseMessagingService {

    @Inject FirebaseMessagingManager fcmManager;
    private UserSessionInterface userSession;
    private RemoteConfig remoteConfig;

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

        remoteConfig = new FirebaseRemoteConfigImpl(getBaseContext());
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
        propagateIDtoServer(newToken);
        //update on the basis of moengage flag
        updateMoEngageToken(newToken);
        Hansel.setNewToken(this, newToken);
        updateApsFlyerToken(newToken);
        ((TkpdCoreRouter) this.getApplicationContext()).refreshFCMFromInstantIdService(newToken);
        try {
            String messageType = "Notification New Token - " + newToken + " | "
                    + userSession.getUserId() + " | " + userSession.getAccessToken() + " | "
                    + Build.FINGERPRINT + " | " + Build.MANUFACTURER + " | "
                    + Build.BRAND + " | " + Build.DEVICE + " | " + Build.PRODUCT + " | " + Build.MODEL
                    + " | " + Build.TAGS;
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", messageType);
            ServerLogger.log(Priority.P2, "TOKEN_REFRESH", messageMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void updateApsFlyerToken(String refreshedToken) {
        TrackApp.getInstance().getAppsFlyer().updateFCMToken(refreshedToken);
    }

    public void updateMoEngageToken(String token) {
        Timber.d("Moengage RefreshedToken: " + token);
        MoengageInteractor.INSTANCE.refreshToken(token);
    }

    public void propagateIDtoServer(String token) {
        if (!TextUtils.isEmpty(token) && isOldGcmUpdate()) {
            String localToken = GCMHandler.getRegistrationId(getApplicationContext());
            if (!localToken.equals(token)) {
                UserSessionInterface userSession = new UserSession(this);
                if (userSession.isLoggedIn()) {
                    IFCMTokenReceiver fcmRefreshTokenReceiver = new FCMTokenReceiver(getBaseContext());
                    FCMTokenUpdate tokenUpdate = new FCMTokenUpdate();
                    tokenUpdate.setOldToken(localToken);
                    tokenUpdate.setNewToken(token);
                    tokenUpdate.setOsType(String.valueOf(1));
                    tokenUpdate.setAccessToken(userSession.getAccessToken());
                    tokenUpdate.setUserId(userSession.getUserId());
                    fcmRefreshTokenReceiver.onTokenReceive(Observable.just(tokenUpdate));
                } else {
                    FCMCacheManager.storeRegId(token, getBaseContext());
                }
            }
        }
    }

    private Boolean isOldGcmUpdate() {
        return remoteConfig.getBoolean(FirebaseMessagingManager.ENABLE_OLD_GCM_UPDATE_SERVICE, false);
    }
}