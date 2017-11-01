package com.tokopedia.core.manage.people.address.listener;

import com.tokopedia.core.manage.people.address.model.districtrecomendation.Address;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public interface DistrictRecomendationFragmentView {

    void showRecommendation(ArrayList<Address> addresses);

    void showLoading();

    void hideLoading();

    interface Constant {
        String INTENT_DATA_PROVINCE = "province";
        String INTENT_DATA_CITY = "city";
        String INTENT_DATA_DICTRICT = "district";
        String INTENT_DATA_ZIP_CODES = "zipCodes";
    }

}
