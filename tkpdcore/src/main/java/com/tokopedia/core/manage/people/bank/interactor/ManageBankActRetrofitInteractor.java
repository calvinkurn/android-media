package com.tokopedia.core.manage.people.bank.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.manage.people.bank.model.ManagePeopleBankResult;

import java.util.Map;

/**
 * Created by Nisie on 6/13/16.
 */
public interface ManageBankActRetrofitInteractor {

    void deleteBank(@NonNull Context context, @NonNull Map<String, String> params,
                            @NonNull ActBankAccountListener listener);

    void defaultBank(@NonNull Context context, @NonNull Map<String, String> params,
                    @NonNull ActBankAccountListener listener);

    void editBank(@NonNull Context context, @NonNull Map<String, String> params,
                    @NonNull ActBankAccountListener listener);

    void addBank(@NonNull Context context, @NonNull Map<String, String> params,
                  @NonNull ActBankAccountListener listener);

    void unsubscribe();

    interface ActBankAccountListener {

        void onSuccess(String status);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }

}
