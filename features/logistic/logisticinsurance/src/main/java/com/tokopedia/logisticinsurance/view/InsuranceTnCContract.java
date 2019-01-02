package com.tokopedia.logisticinsurance.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

public interface InsuranceTnCContract {

    interface View extends CustomerView {
        void showWebView(String webViewData);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadWebViewData();
    }
}
