package com.tokopedia.affiliate.feature.explore.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel;

import java.util.List;

/**
 * @author by yfsx on 24/09/18.
 */
public interface ExploreContract {
    interface View extends CustomerView {

        Context getContext();

        void showLoading();

        void hideLoading();

        void onSuccessGetFirstData(List<Visitable> itemList, String cursor);

        void onErrorGetFirstData(String error);

        void onSuccessGetMoreData(List<Visitable> itemList, String cursor);

        void onErrorGetMoreData(String error);

        void onBymeClicked(ExploreViewModel model);

        void dropKeyboard();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getFirstData(String searchKey);


        void loadMoreData(String cursor, String searchKey);
    }
}
