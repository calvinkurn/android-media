package com.tokopedia.core.manage.people.address.listener;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public interface DistrictRecomendationFragmentView {

    void updateRecommendation();

    void showLoading();

    void hideLoading();

    interface Constant {
        String INTENT_DATA_ADDRESS = "address";
    }

}
