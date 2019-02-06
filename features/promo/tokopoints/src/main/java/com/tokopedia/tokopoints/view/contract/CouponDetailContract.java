package com.tokopedia.tokopoints.view.contract;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokopoints.view.model.CatalogStatusItem;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;

import java.util.List;

public interface CouponDetailContract {

    interface View extends CustomerView {
        void showLoader();

        void showError();

        void openWebView(String url);

        void hideLoader();

        void populateDetail(CouponValueEntity data);

        Context getActivityContext();

        Context getAppContext();

        Resources getResources();

        void showRedeemCouponDialog(String cta, String code, String title);

        void showConfirmRedeemDialog(String cta, String code, String title);

        void showValidationMessageDialog(CatalogsValueEntity item, String title, String message, int resCode);

        void showRedeemFullError(CatalogsValueEntity item, String title, String desc);

        void onRealCodeReFresh(String realCode);

        void onRealCodeReFreshError();

        void onSwipeResponse(CouponSwipeUpdate data, String qrCodeLink, String barCodeLink);

        void onSwipeError(String errorMessage);
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void getCouponDetail(String uniqueCouponCode);

        void reFetchRealCode(String uniqueCatalogCode);

        void swipeMyCoupon(String partnerCode, String pin);
    }
}
