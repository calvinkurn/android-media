package com.tokopedia.tkpd.beranda.presentation.view;

import android.support.annotation.StringRes;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public interface HomeContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void setItems(List<Visitable> items);

        void showNetworkError();

        void removeNetworkError();

        String getString(@StringRes int res);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getHomeData();
    }
}
