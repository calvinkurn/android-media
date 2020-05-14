package com.tokopedia.tokopoints.view.catalogdetail;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokopoints.view.model.CatalogStatusItem;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate;

import java.util.List;

public interface CouponCatalogContract {

    interface View extends CustomerView {
        void showLoader();

        void showError(boolean hasInternet);


        void openWebView(String url);

        void hideLoader();

        void populateDetail(CatalogsValueEntity data);

        Context getActivityContext();

        Context getAppContext();

        Resources getResources();

        void showRedeemCouponDialog(String cta, String code, String title);

        void showConfirmRedeemDialog(String cta, String code, String title);

        void showValidationMessageDialog(CatalogsValueEntity item, String title, String message, int resCode);


        void onSuccessPoints(String point);

        void onErrorPoint(String errorMessage);

        void onRealCodeReFresh(String realCode);

        void onRealCodeReFreshError();

        void refreshCatalog(CatalogStatusItem data);

        void onPreValidateError(String title, String message);

        void gotoSendGiftPage(int id, String title, String pointStr, String banner);

    }

    interface Presenter {

        void getCatalogDetail(String uniqueCatalogCode);

        void fetchLatestStatus(List<Integer> catalogsIds);

        void startSendGift(int id, String title, String pointStr, String banner);
    }
}
