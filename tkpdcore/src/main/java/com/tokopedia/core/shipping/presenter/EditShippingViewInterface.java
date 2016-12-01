package com.tokopedia.core.shipping.presenter;

import android.content.Context;

import com.tokopedia.core.shipping.model.EditShippingModel;
import java.util.ArrayList;

/**
 * Created by Kris on 11/6/2015.
 */
public interface EditShippingViewInterface {

    int JNE_CODE = 1;
    int TIKI_CODE = 2;
    int POS_CODE = 3;
    int MAP_CODE = 10;

    String SHOP_PARAMS_KEY = "shop_params_key";

    String CREATE_SHOP_KEY = "create_shop_key";

    String MAXIMUM_FEE = "maximum_fee";

    void GetCourierData(ArrayList<EditShippingModel.CourierAttribute> shopData, EditShippingModel.ParamEditShop shopParameters);

    void PrepareCourierData(ArrayList<EditShippingModel.CourierAttribute> shopData, EditShippingModel.ParamEditShop shopParameters);

    Context getMainContext();

    void updateOptionsMenu();

    void fillProvinceSpinner(ArrayList<String> provinceList);

    void fillCitySpinner(ArrayList<String> cityList);

    void fillDistrictSpinner(ArrayList<String> districtList);

    void setProvinceSpinnerSelection(int selectedProvince);

    void setCitySpinnerSelection(int selectedCity);

    void setDistrictSpinnerSelection(int selectedDistrict);

    void setCitySpinnerVisibility(int visibility);

    void setDistrictSpinnerVisibility(int visibility);

    void setZipCode(String zipCode);

    void setShopAddress(String shopAddress);

    void finishActivity(String finishMessage);

    void setCourierVisibility(int position, boolean visible);

    int currentDistrictSpinnerPosition();

    int currentCitySpinnerPosition();

    int currentProvinceSpinnerPosition();

    String getZipCode();

    String getShopGoogleMapAddress();

    String getStreetAddress();

    void noPackageTicked();

    void locationIsNotChosen();

    void addressNotFilled();

    void finishLoading();

    void showError(String error);

    void setSpinnerListener();

    boolean chooseLocationDisplayed();

    void setLocationOptionVisibility(int visibility);

    void setGoogleMapAddress(String longitude, String latitude);

    void setCourierOptionsVisibility(int visibility);
}