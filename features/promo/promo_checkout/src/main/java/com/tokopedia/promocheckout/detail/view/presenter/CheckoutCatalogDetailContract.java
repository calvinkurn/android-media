package com.tokopedia.promocheckout.detail.view.presenter;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.promocheckout.detail.model.detailmodel.HachikoCatalogDetail;

import java.util.List;

public interface CheckoutCatalogDetailContract {

    interface View extends CustomerView {
        void showLoader();

        void showError();

        void onEmptyCatalog();

        void openWebView(String url);

        void hideLoader();

        void populateDetail(HachikoCatalogDetail data);

        Context getActivityContext();

        Context getAppContext();

        Resources getResources();

        void showRedeemCouponDialog(String cta, String code, String title);

        void showConfirmRedeemDialog(String cta, String code, String title);

        void showValidationMessageDialog(HachikoCatalogDetail item, String title, String message, int resCode);

        void showRedeemFullError(HachikoCatalogDetail item, String title, String desc);

        void onSuccessPoints(String point);

        void onErrorPoint(String errorMessage);

      //  void onRealCodeReFresh(String realCode);

     //   void onRealCodeReFreshError();

        void onPreValidateError(String title, String message);

    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void getCatalogDetail(String uniqueCatalogCode,int catalog_id);

        void fetchLatestStatus(List<Integer> catalogsIds);
    }
}
