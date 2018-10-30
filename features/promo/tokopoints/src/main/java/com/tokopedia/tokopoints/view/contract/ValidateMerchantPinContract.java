package com.tokopedia.tokopoints.view.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface ValidateMerchantPinContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void onSuccess();

        void onError(String error);

        Context getAppContext();

        Context getActivityContext();

    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void swipeMyCoupon(String partnerCode, String pin);
    }
}
