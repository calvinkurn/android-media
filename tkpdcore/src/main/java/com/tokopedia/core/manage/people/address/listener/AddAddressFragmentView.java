package com.tokopedia.core.manage.people.address.listener;

import android.content.Context;

import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.manage.people.address.fragment.adapter.ProvinceAdapter;
import com.tokopedia.core.manage.people.address.fragment.adapter.RegencyAdapter;
import com.tokopedia.core.manage.people.address.model.Destination;

import java.util.List;
import java.util.Map;

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

}
