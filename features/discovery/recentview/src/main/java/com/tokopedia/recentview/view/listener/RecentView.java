package com.tokopedia.recentview.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 7/4/17.
 */

public interface RecentView {
    interface View extends CustomerView {
        void showLoading();

        void showLoadingProgress();

        void dismissLoadingProgress();

        void onErrorGetRecentView(String errorMessage);

        void onEmptyGetRecentView();

        void sendRecentViewClickTracking(RecentViewDetailProductDataModel element);


        Context getContext();
    }
}