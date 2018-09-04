package com.tokopedia.mitra.homepage.contract;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface MitraParentHomepageContract {
    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}
