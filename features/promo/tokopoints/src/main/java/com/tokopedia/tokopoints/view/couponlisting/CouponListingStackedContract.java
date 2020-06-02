package com.tokopedia.tokopoints.view.couponlisting;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

import java.util.Map;

public interface CouponListingStackedContract {

    interface View extends CustomerView {
        void showLoader();


        void hideLoader();

        void openWebView(String url);


        void emptyCoupons(Map<String, String> errors);


        Context getActivityContext();

        Context getAppContext();

        Resources getResources();

//        void showRedeemCouponDialog(String cta, String code, String title);

//        void showCouponInStackBottomSheet(String stackId);
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void getCoupons(int categoryId);

        void setCategoryId(int categoryId);

        void getCouponInStack(String stackId);
    }
}
