package com.tokopedia.core.session;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by stevenfredian on 12/30/16.
 */

public interface VerifyPhoneInteractor {

    void verifyPhone(Context context, String phoneNumber, String userId, VerifyPhoneListener verifyPhone);

    void unSubscribe();

    interface VerifyPhoneListener {
        void onError(String error);

        void onThrowable(Throwable e);

        void onTimeout();

        void onSuccess(int result, String uuid);
    }
}
