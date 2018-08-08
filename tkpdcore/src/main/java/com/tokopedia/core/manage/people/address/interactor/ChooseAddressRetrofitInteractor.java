package com.tokopedia.core.manage.people.address.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.manage.people.address.model.ChooseAddress.ChooseAddressResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * Created by Alifa on 10/11/2016.
 */

public interface ChooseAddressRetrofitInteractor {
    void getAddresses(@NonNull Context context, @NonNull TKPDMapParam<String, String> params,
                      @NonNull ChooseAddressListener listener);
    void unsubscribe();

    void setRequesting(boolean isRequesting);

    boolean isRequesting();

    interface ChooseAddressListener {

        void onSuccess(@NonNull ChooseAddressResponse data);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }
}
