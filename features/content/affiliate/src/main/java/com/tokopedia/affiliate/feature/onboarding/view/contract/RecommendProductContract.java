package com.tokopedia.affiliate.feature.onboarding.view.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by milhamj on 10/4/18.
 */
public interface RecommendProductContract {
    interface View extends CustomerView {
        Context getContext();

        void onSuccessGetProductInfo();

        void onErrorGetProductInfo(String message);

        void showLoading();

        void hideLoading();

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}
