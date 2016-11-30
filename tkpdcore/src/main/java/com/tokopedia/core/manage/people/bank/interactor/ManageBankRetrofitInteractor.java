package com.tokopedia.core.manage.people.bank.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.manage.people.bank.model.ManagePeopleBankResult;

import java.util.Map;

/**
 * Created by Nisie on 6/10/16.
 */
public interface ManageBankRetrofitInteractor {

    void getBankAccountList(@NonNull Context context, @NonNull Map<String, String> params,
                         @NonNull GetBankAccountListener listener);

    void unsubscribe();

    interface GetBankAccountListener {

        void onSuccess(@NonNull ManagePeopleBankResult data);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }
}
