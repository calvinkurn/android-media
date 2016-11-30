package com.tokopedia.core.msisdn.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.msisdn.model.VerificationForm;

import java.util.Map;

/**
 * Created by Nisie on 7/13/16.
 */
public interface MsisdnVerificationRetrofitInteractor {

    void checkVerification(@NonNull Context context,
                    @NonNull Map<String, String> params,
                    @NonNull CheckVerificationListener listener);

    void requestOTP(@NonNull Context context,
                    @NonNull Map<String, String> params,
                    @NonNull RequestOTPListener listener);

    void verifyOTP(@NonNull Context context,
                    @NonNull Map<String, String> params,
                    @NonNull VerifyOTPListener listener);

    void unSubscribeObservable();

    interface RequestOTPListener {
        void onSuccess();

        void onTimeout();

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String error);

        void onNullData();

        void onNoConnection();
    }


    interface VerifyOTPListener {
        void onSuccess();

        void onTimeout();

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String error);

        void onNullData();

        void onNoConnection();
    }

    interface CheckVerificationListener {
        void onSuccess(VerificationForm result);

        void onTimeout();

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String error);

        void onNullData();

        void onNoConnection();
    }
}
