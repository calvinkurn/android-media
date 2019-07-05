package com.tokopedia.logisticaddaddress.features.district_recommendation;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.logisticdata.data.entity.address.Token;

/**
 * Created by Irfan Khoirul on 17/11/18.
 */

public interface DistrictRecommendationContract {

    interface View extends BaseListViewListener<AddressViewModel> {

        void showLoading();

        void hideLoading();

        void showGetListError(Throwable throwable);

        void showNoResultMessage();

        void showInitialLoadMessage();
    }

    interface Presenter extends CustomerPresenter<View> {

        void loadData(String query, Token token, int page);

    }

    interface Constant {
        String INTENT_DISTRICT_RECOMMENDATION_ADDRESS = "district_recommendation_address";
        String INTENT_DATA_ADDRESS = "address";
        String ARGUMENT_DATA_TOKEN = "token";
    }
}
