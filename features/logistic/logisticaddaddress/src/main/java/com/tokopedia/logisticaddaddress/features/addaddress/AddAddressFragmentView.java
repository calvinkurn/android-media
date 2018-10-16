package com.tokopedia.logisticaddaddress.features.addaddress;

import android.content.Context;

import com.tokopedia.logisticaddaddress.adapter.ProvinceAdapter;
import com.tokopedia.logisticaddaddress.adapter.RegencyAdapter;
import com.tokopedia.logisticdata.data.entity.address.db.City;
import com.tokopedia.logisticdata.data.entity.address.db.District;
import com.tokopedia.logisticdata.data.entity.address.db.Province;
import com.tokopedia.logisticdata.data.entity.address.Destination;

import java.util.List;

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

    void setActionsEnabled(boolean actionsEnabled);

    String getPassword();

    boolean isValidAddress();

    Destination getAddress();

    void setAddress(Destination address);

    void setProvince(List<Province> provinces);

    void resetRegency();

    void hideSubDistrict();

    void resetSubDistrict();

    ProvinceAdapter getProvinceAdapter();

    RegencyAdapter getRegencyAdapter();

    void showLoadingRegency();

    void setCity(List<City> cities);

    void changeProvince(List<City> cities);

    void showLoadingDistrict();

    void setDistrict(List<District> districts);

    void errorSaveAddress();

    void successSaveAddress();

    void setPinpointAddress(String address);

}
