package com.tokopedia.tokopoints.view.cataloglisting;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.tokopoints.view.model.CatalogBanner;
import com.tokopedia.tokopoints.view.model.CatalogFilterBase;

import java.util.List;

public interface CatalogListingContract {

    interface View extends CustomerView {
        void showLoader();

        void onErrorBanners(String errorMessage);

        void onSuccessBanners(List<CatalogBanner> banners);

        void onErrorPoint(String errorMessage);

        void hideLoader();

        void gotoMyCoupons();

        void openWebView(String url);

        void onSuccessFilter(CatalogFilterBase filters);

        void onErrorFilter(String errorMessage, boolean hasInternet);

        Context getActivityContext();

        Context getAppContext();

        Resources getResources();

        void refreshTab();

        void onSuccessPoints(String rewardStr, int rewardValue, String membership, String eggUrl);
    }

    interface Presenter {


        void getHomePageData(String slugCategory, String slugSubCategory, boolean isBannerRequire);

        void getPointData();

        void setPointRangeId(int id);

        int getPointRangeId();

        void setCurrentCategoryId(int id);

        int getCurrentCategoryId();

        void setCurrentSubCategoryId(int id);

        int getCurrentSubCategoryId();
    }
}
