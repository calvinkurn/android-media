package com.tokopedia.tokopoints.view.cataloglisting;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

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

        void onPreValidateError(String title, String message);

        void gotoSendGiftPage(int id, String title, String pointStr);

    }

    interface Presenter {

        void fetchLatestStatus(List<Integer> catalogsIds);
    }
}
