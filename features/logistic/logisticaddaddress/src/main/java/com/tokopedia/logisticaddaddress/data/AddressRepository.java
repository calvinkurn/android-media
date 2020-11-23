package com.tokopedia.logisticaddaddress.data;

import androidx.annotation.NonNull;

import com.tokopedia.logisticCommon.data.entity.address.FormAddressDomainModel;
import com.tokopedia.logisticCommon.data.entity.address.db.Province;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by nisie on 9/6/16.
 */
public interface AddressRepository {

    void addAddress(@NonNull Map<String, String> params, @NonNull AddAddressListener listener);

    void editAddress(@NonNull Map<String, String> params, @NonNull AddAddressListener listener);

    void unsubscribe();

    interface AddAddressListener {

        void onSuccess(String address_id);

        void onTimeout();

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
