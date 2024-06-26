package com.tokopedia.explore.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.explore.view.uimodel.ExploreViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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

        void updateCategoryId(long categoryId);

        void updateSearch(String search);

        void clearData();

        void onCategoryClicked(int position, long categoryId, String categoryName, android.view.View view);

        void onCategoryReset();

        void showRefreshing();

        void showLoading();

        void dismissLoading();

        void showEmpty();

        void goToKolPostDetail(@NotNull String postId, @NotNull String name, long recomId);

        void addExploreItemCoachmark(android.view.View view);

        void dropKeyboard();

        void scrollToTop();

        void resetDataParam();

        void stopTrace();

        void onAffiliateTrack(List<TrackingModel> trackingList, boolean isClick);

        long getExploreCategory();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getExploreData(boolean clearData);

        void updateCursor(String cursor);

        void updateCategoryId(long categoryId);

        void updateSearch(String search);

        void trackAffiliate(String url);

        void appendImpressionTracking(@NotNull String url);

        void onPullToRefreshTriggered();
    }
}
