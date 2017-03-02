package com.tokopedia.core.otp;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by nisie on 12/20/16.
 */

public interface OTPRetrofitInteractor {

    void requestOTP(@NonNull Context context,
                    @NonNull Map<String, String> params,
                    @NonNull OTPRetrofitInteractor.RequestOTPListener listener);

    void requestOTPWithCall(@NonNull Context context,
                            @NonNull Map<String, String> params,
                            @NonNull OTPRetrofitInteractor.RequestOTPWithCallListener listener);

    void verifyOTP(@NonNull Context context,
                   @NonNull Map<String, String> params,
                   @NonNull OTPRetrofitInteractor.VerifyOTPListener listener);

    void unsubscribeObservable();

    interface RequestOTPWithCallListener {
        void onSuccess(String message);

        void onTimeout();

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String error);

        void onNullData();

        void onNoConnection();
    }

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


}
