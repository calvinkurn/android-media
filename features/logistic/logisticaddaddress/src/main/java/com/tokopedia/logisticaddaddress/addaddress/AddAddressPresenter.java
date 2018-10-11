package com.tokopedia.logisticaddaddress.addaddress;


import com.tokopedia.logisticaddaddress.model.db.City;
import com.tokopedia.logisticaddaddress.model.db.Province;

/**
 * Created by nisie on 9/6/16.
 */
public interface AddAddressPresenter {

    void attachView();

    void detachView();

    void saveAddress();

    void getListProvince();

    void onProvinceSelected(int pos);

    void onEditProvinceSelected(int pos);

    void onRegencySelected(int pos);

    void getListCity(Province province);

    void provinceChanged(Province province);

    void getListDistrict(City city);

}
