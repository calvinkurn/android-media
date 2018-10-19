package com.tokopedia.kelontongapp.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by meta on 16/10/18.
 */
public class FirebaseIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM", "RefreshedToken: " + refreshedToken);
        Preference.saveFcmToken(this, refreshedToken);
    }
}
