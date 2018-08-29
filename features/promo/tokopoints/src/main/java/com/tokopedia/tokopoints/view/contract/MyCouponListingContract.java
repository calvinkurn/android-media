package com.tokopedia.tokopoints.view.contract;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokopoints.view.model.CatalogBanner;
import com.tokopedia.tokopoints.view.model.CatalogFilterBase;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;

import java.util.List;

public interface MyCouponListingContract {

    interface View extends CustomerView {
        void showLoader();

        void showError(String errorMessage);

        void hideLoader();

        void openWebView(String url);

        void populateCoupons(List<CouponValueEntity> coupons);

        void onErrorCoupons(String errorMessage);

        Context getActivityContext();

        Context getAppContext();

        Resources getResources();

        void showRedeemCouponDialog(String cta, String code, String title);
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void getCoupons();
    }
}
