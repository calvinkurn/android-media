package com.tokopedia.core.manage.people.address.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.manage.people.address.model.FormAddressDomainModel;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by nisie on 9/6/16.
 */
public interface AddAddressRetrofitInteractor {

    void addAddress(@NonNull Context context, @NonNull Map<String, String> params,
                         @NonNull AddAddressListener listener);

    void editAddress(@NonNull Context context, @NonNull Map<String, String> params,
                    @NonNull AddAddressListener listener);

    void getListProvince(@NonNull Context context, @NonNull Map<String, String> params,
                     @NonNull GetListProvinceListener listener);

    void getListDistrict(@NonNull Context context, @NonNull String cityId,
                     @NonNull GetListDistrictListener listener);

    void getListCity(@NonNull Context context, @NonNull String provinceID,
                     @NonNull GetListCityListener listener);

    void unsubscribe();

    interface AddAddressListener {

        void onSuccess(String address_id);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }

    interface GetListProvinceListener {

        void onSuccess(ArrayList<Province> provinces);

        void onTimeout();

        void onError(String error);

        void onNullData();

    }

    interface GetListCityListener {

        void onSuccess(FormAddressDomainModel cities);

        void onTimeout();

        void onError(String error);

        void onNullData();

    }

    interface GetListDistrictListener {

        void onSuccess(FormAddressDomainModel cities);

        void onTimeout();

        void onError(String error);

        void onNullData();

    }
}
