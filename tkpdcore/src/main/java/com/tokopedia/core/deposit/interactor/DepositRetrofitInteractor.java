package com.tokopedia.core.deposit.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.deposit.model.SummaryWithdraw;

import java.util.Map;

/**
 * Created by Nisie on 4/7/16.
 */
public interface DepositRetrofitInteractor {

    void getSummaryDeposit(@NonNull Context context, @NonNull Map<String, String> params,
                    @NonNull DepositListener listener);

    void unsubscribe();

    void setRequesting(boolean isRequesting);

    boolean isRequesting();

    interface DepositListener {

        void onSuccess(@NonNull SummaryWithdraw data);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }
}
