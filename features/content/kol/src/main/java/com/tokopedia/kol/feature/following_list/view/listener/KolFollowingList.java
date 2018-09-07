package com.tokopedia.kol.feature.following_list.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingResultViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingViewModel;

/**
 * Created by yfsx on 28/12/17.
 */

public interface KolFollowingList {

    interface View extends CustomerView {

        Context getContext();

        void showLoading();

        void hideLoading();

        void onSuccessGetKolFollowingList(KolFollowingResultViewModel itemList);

        void onSuccessGetKolFollowingListEmptyState();

        void onErrorGetKolFollowingList(String error);

        void onSuccessLoadMoreKolFollowingList(KolFollowingResultViewModel itemList);

        void onErrorLoadMoreKolFollowingList(String error);

        void onListItemClicked(KolFollowingViewModel item);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getKolFollowingList(int userId);

        void getKolLoadMore(int userId, String cursor);
    }
}
