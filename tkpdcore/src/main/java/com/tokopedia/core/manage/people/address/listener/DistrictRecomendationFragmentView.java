package com.tokopedia.core.manage.people.address.listener;

import android.support.annotation.NonNull;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public interface DistrictRecomendationFragmentView {

    void updateRecommendation();

    void notifyClearAdapter();

    void showLoading();

    void hideLoading();

    void showMessage(String message);

    void hideMessage();

    void showNoConnection(@NonNull String message);

    interface Constant {
        String INTENT_DATA_ADDRESS = "address";
        String ARGUMENT_DATA_TOKEN = "token";
    }

}
