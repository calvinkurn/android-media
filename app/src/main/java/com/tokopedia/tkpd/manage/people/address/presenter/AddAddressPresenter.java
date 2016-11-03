package com.tokopedia.tkpd.manage.people.address.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.tkpd.database.model.City;
import com.tokopedia.tkpd.database.model.Province;

/**
 * Created by nisie on 9/6/16.
 */
public interface AddAddressPresenter {
    void saveAddress();

    void getListProvince();

    void onProvinceSelected(int pos);

    void onRegencySelected(int pos);

    LatLng getLatLng();

    void setLatLng(String latitude, String longitude);

    void getListCity(Province province);

    void getListDistrict(City city);

}
