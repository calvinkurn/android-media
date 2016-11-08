package com.tokopedia.core.deposit.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.deposit.model.WithdrawForm;

import java.util.Map;

/**
 * Created by Nisie on 4/13/16.
 */
public interface WithdrawRetrofitInteractor {

    void getWithdrawForm(@NonNull Context context, @NonNull Map<String, String> params,
                           @NonNull WithdrawFormListener listener);

    void doWithdraw(@NonNull Context context, @NonNull Map<String, String> params,
                         @NonNull DoWithdrawListener listener);

    void sendOTP(@NonNull Context context, @NonNull Map<String, String> params,
                    @NonNull SendOTPListener listener);

    void unsubscribe();

    interface WithdrawFormListener {

        void onSuccess(@NonNull WithdrawForm data);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }

    interface DoWithdrawListener {

        void onSuccess();

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }


    interface SendOTPListener {

        void onSuccess(String message);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }
}
