package com.tokopedia.core.gcm;

import android.os.Build;
import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.moengage.push.PushManager;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import io.hansel.hanselsdk.Hansel;
import rx.Observable;
import timber.log.Timber;

/**
 * @author by Herdi_WORK on 09.12.16.
 */

public class FCMInstanceIDService extends FirebaseInstanceIdService implements IFCMInstanceIDService {

    private static final String TAG = FCMInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        CommonUtils.dumper(TAG + " RefreshedToken: " + refreshedToken);
        propagateIDtoServer(refreshedToken);
        updateMoEngageToken(refreshedToken);
        Hansel.setNewToken(this, refreshedToken);
        updateApsFlyerToken(refreshedToken);
        ((TkpdCoreRouter) this.getApplicationContext()).refreshFCMFromInstantIdService(refreshedToken);

        try {
            UserSessionInterface userSession = new UserSession(this);
            Timber.w("P2" + "Notification Refresh Token - " + refreshedToken + " | "
                    + userSession.getUserId() + " | " + userSession.getAccessToken() + " | "
                    + Build.FINGERPRINT + " | " + Build.MANUFACTURER + " | "
                    + Build.BRAND + " | " + Build.DEVICE + " | " + Build.PRODUCT + " | " + Build.MODEL
                    + " | " + Build.TAGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateApsFlyerToken(String refreshedToken) {
        TrackApp.getInstance().getAppsFlyer().updateFCMToken(refreshedToken);
    }

    @Override
    public void updateMoEngageToken(String token) {
        CommonUtils.dumper("Moengage RefreshedToken: " + token);
        PushManager.getInstance().refreshToken(getApplicationContext(), token);
    }

    @Override
    public void propagateIDtoServer(String token) {
        if (!TextUtils.isEmpty(token)) {
            String localToken = GCMHandler.getRegistrationId(getApplicationContext());
            CommonUtils.dumper(TAG + " RefreshedToken: " + token + ", localToken: " + localToken);
            if (!localToken.equals(token)) {
                SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getApplicationContext()).legacySessionHandler();
                if (sessionHandler.isV4Login()) {
                    IFCMTokenReceiver fcmRefreshTokenReceiver = new FCMTokenReceiver(getBaseContext());
                    FCMTokenUpdate tokenUpdate = new FCMTokenUpdate();
                    tokenUpdate.setOldToken(localToken);
                    tokenUpdate.setNewToken(token);
                    tokenUpdate.setOsType(String.valueOf(1));
                    tokenUpdate.setAccessToken(sessionHandler.getAccessToken());
                    tokenUpdate.setUserId(sessionHandler.getLoginID());
                    fcmRefreshTokenReceiver.onTokenReceive(Observable.just(tokenUpdate));
                } else {
                    FCMCacheManager.storeRegId(token, getBaseContext());
                }
            }
        }
    }
}
