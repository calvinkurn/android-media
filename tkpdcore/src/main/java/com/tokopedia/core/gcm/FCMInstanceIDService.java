package com.tokopedia.core.gcm;

import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.moengage.push.PushManager;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;
import com.tokopedia.core.util.SessionHandler;

import rx.Observable;

/**
 * @author by Herdi_WORK on 09.12.16.
 */

public class FCMInstanceIDService extends FirebaseInstanceIdService implements IFCMInstanceIDService {

    private static final String TAG = FCMInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        CommonUtils.dumper(TAG + " RefreshedToken: " + refreshedToken);
        updateMoEngageToken(refreshedToken);
        propagateIDtoServer(refreshedToken);
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
                SessionHandler sessionHandler = new SessionHandler(getApplicationContext());
                if (sessionHandler.isV4Login()) {
                    IFCMTokenReceiver fcmRefreshTokenReceiver = new FCMTokenReceiver(getBaseContext());
                    FCMTokenUpdate tokenUpdate = new FCMTokenUpdate();
                    tokenUpdate.setOldToken(localToken);
                    tokenUpdate.setNewToken(token);
                    tokenUpdate.setOsType(String.valueOf(1));
                    tokenUpdate.setAccessToken(sessionHandler.getAccessToken(this));
                    tokenUpdate.setUserId(sessionHandler.getLoginID());
                    fcmRefreshTokenReceiver.onTokenReceive(Observable.just(tokenUpdate));
                } else {
                    FCMCacheManager.storeRegId(token, getBaseContext());
                }
            }
        }
    }
}
