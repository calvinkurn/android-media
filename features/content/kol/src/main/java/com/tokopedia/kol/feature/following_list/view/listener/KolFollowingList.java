package com.tokopedia.kol.feature.following_list.view.listener;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.kol.feature.following_list.view.viewmodel.FollowingResultViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.FollowingViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingResultViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingViewModel;

/**
 * Created by yfsx on 28/12/17.
 */

public interface KolFollowingList<R extends FollowingViewModel, T extends FollowingResultViewModel<R>> {

    interface View<R, T> extends CustomerView {

        Context getContext();

        void showLoading();

        void hideLoading();

        void onSuccessGetKolFollowingList(T viewModel);

        void onSuccessGetKolFollowingListEmptyState();

        void onErrorGetKolFollowingList(String error);

        void onSuccessLoadMoreKolFollowingList(T itemList);

        void onErrorLoadMoreKolFollowingList(String error);

        void onListItemClicked(@NonNull R item);

        boolean isOpenFollowerPage();

        void onUnfollowShopButtonClicked(@NonNull FollowingViewModel model);

        void onSuccessUnfollowShop(@NonNull FollowingViewModel model);

        void onErrorUnfollowShop(@NonNull String errorMessage);
    }

    interface Presenter<R, T> extends CustomerPresenter<View<R, T>> {
        void getKolFollowingList(int userId);

        void getKolLoadMore(int userId, String cursor);

        void getFollowersList(int userId);

        void getFollowersLoadMore(int userId, String cursor);

        void unfollowShop(@NonNull FollowingViewModel model);
    }
}
