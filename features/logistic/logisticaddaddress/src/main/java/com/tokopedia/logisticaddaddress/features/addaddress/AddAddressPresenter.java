package com.tokopedia.logisticaddaddress.features.addaddress;


import android.content.Context;

import com.tokopedia.logisticdata.data.entity.address.Destination;

/**
 * Created by nisie on 9/6/16.
 */
public interface AddAddressPresenter {

    void attachView(AddAddressFragmentView view);

    void detachView();

    void saveAddress();

    void requestReverseGeoCode(Context context, Destination destination);

}
