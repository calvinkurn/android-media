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

public interface CouponCatalogContract {

    interface View extends CustomerView {
        void showLoader();

        void showError();

        void onEmptyCatalog();

        void openWebView(String url);

        void hideLoader();

        void populateDetail(CatalogsValueEntity data);

        void populateDetail(CouponValueEntity data);

        Context getActivityContext();

        Context getAppContext();

        Resources getResources();

        void showRedeemCouponDialog(String cta, String code, String title);

        void showConfirmRedeemDialog(String cta, String code, String title);

        void showValidationMessageDialog(CatalogsValueEntity item, String title, String message, int resCode);

        void showRedeemFullError(CatalogsValueEntity item, String title, String desc);

        void onSuccessPoints(String point);

        void onErrorPoint(String errorMessage);

        void onRealCodeReFresh(String realCode);

        void onRealCodeReFreshError();

        void refreshCatalog(CatalogStatusItem data);

        void onPreValidateError(String title, String message);

        void gotoSendGiftPage(int id, String title, String pointStr);

        void onSwipeResponse(CouponSwipeUpdate data, String qrCodeLink, String barCodeLink);

        void onSwipeError(String errorMessage);

        void onFinishRendering();
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void getCouponDetail(String uniqueCouponCode);

        void getCatalogDetail(String uniqueCatalogCode);

        void reFetchRealCode(String uniqueCatalogCode);

        void fetchLatestStatus(List<Integer> catalogsIds);

        void swipeMyCoupon(String partnerCode, String pin);

        void startSendGift(int id, String title, String pointStr);
    }
}
