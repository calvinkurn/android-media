package com.tokopedia.core.manage.general.districtrecommendation.view;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Address;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 17/11/17.
 */

public interface DistrictRecommendationContract {

    interface View extends CustomerView {
        Context getActivity();

        void updateRecommendation();

        void notifyUpdateAdapter();

        void setInitialLoading();

        void setLoadMoreLoading();

        void showLoading();

        void hideLoading();

        void showMessage(String message);

        void hideMessage();

        void showNoConnection(@NonNull String message);
    }

    interface Presenter extends CustomerPresenter<View> {
        void setToken(Token token);

        void searchAddress(String keyword);

        void searchNextIfAvailable(String keyword);

        ArrayList<Address> getAddresses();

        void clearData();
    }

    interface Constant {
        String INTENT_DATA_ADDRESS = "address";
        String ARGUMENT_DATA_TOKEN = "token";
    }
}
