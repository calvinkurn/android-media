package com.tokopedia.tokopoints.view.contract;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokopoints.view.model.CatalogBanner;
import com.tokopedia.tokopoints.view.model.CatalogFilterBase;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;

import java.util.ArrayList;
import java.util.List;

public interface CatalogListingContract {

    interface View extends CustomerView {
        void showLoader();

        void onErrorBanners(String errorMessage);

        void onSuccessBanners(List<CatalogBanner> banners);

        void onSuccessPoints(String point);

        void onErrorPoint(String errorMessage);

        void hideLoader();

        void gotoMyCoupons();

        void openWebView(String url);

        void onSuccessFilter(CatalogFilterBase filters);

        void onErrorFilter(String errorMessage);

        Context getActivityContext();

        Context getAppContext();

        Resources getResources();

        void refreshTab(int categoryId);

        int getSelectedCategoryId();

        void updateSelectedCategoryId(int id);

        boolean isAddedView();
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void getHomePageData();

        void getPointData();

        int getSelectedCategoryId();
    }
}
