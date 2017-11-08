package com.tokopedia.core.manage.people.address.listener;

import android.support.annotation.Nullable;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public interface DistrictRecomendationFragmentView {

    void updateRecommendation();

    void showLoading();

    void hideLoading();

    void showMessage(String message);

    void hideMessage();

    void showNoConnection(@Nullable String message);

    interface Constant {
        String INTENT_DATA_ADDRESS = "address";
        String ARGUMENT_DATA_TOKEN = "token";
    }

}
