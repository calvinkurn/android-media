package com.tokopedia.affiliate.feature.onboarding.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.affiliate.feature.onboarding.view.viewmodel.RecommendProductViewModel;
import com.tokopedia.user.session.UserSession;

/**
 * @author by milhamj on 10/4/18.
 */
public interface RecommendProductContract {
    interface View extends CustomerView {
        Context getContext();

        UserSession getUserSession();

        void onSuccessGetProductInfo(RecommendProductViewModel viewModel);

        void onErrorGetProductInfo(String message);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getProductInfo(String productId);
    }
}
