package com.tokopedia.core.gcm;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.localytics.android.Localytics;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.gcm.model.FcmTokenUpdate;

import rx.Observable;

/**
 * Created by Herdi_WORK on 09.12.16.
 */

public class FCMInstanceIDService extends FirebaseInstanceIdService implements IFCMInstanceIDService {

    private static final String TAG = FCMInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        CommonUtils.dumper(TAG + "RefreshedToken: " + refreshedToken);
        updateLocalyticsPushRegistrationID(refreshedToken);
        propagateIDtoServer(refreshedToken);
    }

    @Override
    public void updateLocalyticsPushRegistrationID(String token) {
        Localytics.setPushRegistrationId(token);
    }

    @Override
    public void propagateIDtoServer(String token) {
        if (!TextUtils.isEmpty(token)) {
            String localToken = GCMHandler.getRegistrationId(getApplicationContext());
            Log.d(TAG, "RefreshedToken: " + token + ", localToken: " + localToken);
            if (!localToken.equals(token)) {
                IFcmRefreshTokenReceiver fcmRefreshTokenReceiver = new FcmRefreshTokenReceiver(this.getApplication());
                FcmTokenUpdate tokenUpdate = new FcmTokenUpdate();
                tokenUpdate.setOldToken(localToken);
                tokenUpdate.setNewToken(token);
                tokenUpdate.setOsType(String.valueOf(2));
                fcmRefreshTokenReceiver.onTokenReceive(Observable.just(tokenUpdate));
            }
        }
    }
}
