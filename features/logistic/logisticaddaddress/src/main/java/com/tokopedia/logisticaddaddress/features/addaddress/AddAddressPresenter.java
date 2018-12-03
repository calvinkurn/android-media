package com.tokopedia.logisticaddaddress.features.addaddress;


import android.content.Context;

import com.tokopedia.logisticdata.data.entity.address.Destination;
import com.tokopedia.logisticdata.data.entity.address.db.City;
import com.tokopedia.logisticdata.data.entity.address.db.Province;

/**
 * Created by nisie on 9/6/16.
 */
public interface AddAddressPresenter {

    void attachView(AddAddressFragmentView view);

    void detachView();

    void saveAddress();

    void onProvinceSelected(int pos);

    void onEditProvinceSelected(int pos);

    void onRegencySelected(int pos);

    void requestReverseGeoCode(Context context, Destination destination);

}
