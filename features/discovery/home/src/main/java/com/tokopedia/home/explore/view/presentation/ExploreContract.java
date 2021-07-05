package com.tokopedia.home.explore.view.presentation;


import android.content.Context;
import androidx.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.explore.view.adapter.datamodel.ExploreSectionDataModel;

import java.util.List;

/**
 * Created by errysuprayogi on 1/30/18.
 */

public interface ExploreContract {

    interface View extends CustomerView {

        void showLoading();

        void hideLoading();

        void showNetworkError(String message);

        void removeNetworkError();

        String getString(@StringRes int res);

        void renderData(List<ExploreSectionDataModel> list);

        Context getViewContext();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getData();

    }

}