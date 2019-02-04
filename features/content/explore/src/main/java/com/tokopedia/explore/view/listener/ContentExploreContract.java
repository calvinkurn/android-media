package com.tokopedia.explore.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.explore.view.viewmodel.ExploreViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

/**
 * @author by milhamj on 20/07/18.
 */

public interface ContentExploreContract {

    interface View extends CustomerView {
        Context getContext();

        void onSuccessGetExploreData(ExploreViewModel exploreViewModel, boolean clearData);

        void onErrorGetExploreDataFirstPage(String message);

        void onErrorGetExploreDataMore();

        void updateCursor(String cursor);

        void updateCategoryId(int categoryId);

        void updateSearch(String search);

        void clearData();

        void onCategoryClicked(int position, int categoryId, String categoryName);

        void onCategoryReset();

        void showRefreshing();

        void showLoading();

        void dismissLoading();

        void showEmpty();

        void goToKolPostDetail(KolPostViewModel kolPostViewModel);

        void dropKeyboard();

        void scrollToTop();

        void resetDataParam();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getExploreData(boolean clearData);

        void updateCursor(String cursor);

        void updateCategoryId(int categoryId);

        void updateSearch(String search);
    }
}
