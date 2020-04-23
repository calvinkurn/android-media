package com.tokopedia.tokopoints.view.cataloglisting;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokopoints.view.model.CatalogStatusItem;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;

import java.util.List;

public interface CatalogListItemContract {

    interface View extends CustomerView {
        void showLoader();

        void showError();

        void onEmptyCatalog();

        void openWebView(String url);

        void hideLoader();


        Context getActivityContext();

        Context getAppContext();

        Resources getResources();


        int getCurrentCategoryId();

        int getCurrentSubCategoryId();

        void showRedeemCouponDialog(String cta, String code, String title);

        void showConfirmRedeemDialog(String cta, String code, String title);

        void showValidationMessageDialog(CatalogsValueEntity item, String title, String message, int resCode);

        void refreshCatalog(List<CatalogStatusItem> items);

        void showRedeemFullError(CatalogsValueEntity item, String title, String desc);

        void onPreValidateError(String title, String message);

        void gotoSendGiftPage(int id, String title, String pointStr);

        void populateCatalog(int categoryId, int subCategoryId, int pointRange, boolean showLoader);
    }

    interface Presenter {


        void fetchLatestStatus(List<Integer> catalogsIds);
    }
}
