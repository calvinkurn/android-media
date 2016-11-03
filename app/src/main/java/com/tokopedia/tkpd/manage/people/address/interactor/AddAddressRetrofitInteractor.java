package com.tokopedia.tkpd.manage.people.address.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by nisie on 9/6/16.
 */
public interface AddAddressRetrofitInteractor {

    void addAddress(@NonNull Context context, @NonNull Map<String, String> params,
                         @NonNull AddAddressListener listener);

    void editAddress(@NonNull Context context, @NonNull Map<String, String> params,
                    @NonNull AddAddressListener listener);

    void unsubscribe();

    interface AddAddressListener {

        void onSuccess(String address_id);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }
}
