package com.tokopedia.logisticaddaddress.features.addaddress;

import android.content.Context;

import com.tokopedia.logisticdata.data.entity.address.Destination;

/**
 * Created by nisie on 9/6/16.
 */
public interface AddAddressFragmentView {

    Context context();

    boolean isEdit();

    boolean isDistrictRecommendation();

    void showLoading();

    void finishLoading();

    void finishActivity();

    void showErrorSnackbar(String errorMessage);

    String getPassword();

    boolean isValidAddress();

    Destination getAddress();

    void setAddress(Destination address);

    void errorSaveAddress();

    void successSaveAddress();

    void setPinpointAddress(String address);

}
