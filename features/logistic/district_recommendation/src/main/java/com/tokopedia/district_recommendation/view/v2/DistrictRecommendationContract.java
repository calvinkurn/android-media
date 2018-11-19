package com.tokopedia.district_recommendation.view.v2;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.district_recommendation.domain.model.Token;

import java.util.List;

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
}
