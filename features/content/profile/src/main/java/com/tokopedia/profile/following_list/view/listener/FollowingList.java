package com.tokopedia.profile.following_list.view.listener;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.profile.following_list.view.fragment.BaseFollowListFragment;
import com.tokopedia.profile.following_list.view.viewmodel.FollowingResultViewModel;
import com.tokopedia.profile.following_list.view.viewmodel.FollowingViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * Created by yfsx on 28/12/17.
 */

public interface FollowingList<R extends FollowingViewModel, T extends FollowingResultViewModel<R>> {

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

        void onUnfollowShopButtonClicked(@NonNull FollowingViewModel model);

        void onSuccessUnfollowShop(@NonNull FollowingViewModel model);

        void onErrorUnfollowShop(@NonNull String errorMessage);
    }

    interface Presenter<R, T> extends CustomerPresenter<View<R, T>> {
        void getFollowList(int userId, @NotNull BaseFollowListFragment.FollowListType type);

        void getFollowListLoadMore(int userId, String cursor, @NotNull BaseFollowListFragment.FollowListType type);

        void unfollowShop(@NonNull FollowingViewModel model);
    }
}
