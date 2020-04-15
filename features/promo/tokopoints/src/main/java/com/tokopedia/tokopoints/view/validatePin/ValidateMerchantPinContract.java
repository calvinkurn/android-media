package com.tokopedia.tokopoints.view.validatePin;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate;

public interface ValidateMerchantPinContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void onSuccess(CouponSwipeUpdate couponSwipeUpdate);

        void onError(String error);

        Context getAppContext();

        Context getActivityContext();

    }

    interface Presenter {
        void swipeMyCoupon(String partnerCode, String pin);
    }
}
