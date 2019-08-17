package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;

public interface SearchContract {
    interface View extends CustomerView {

        BaseAppComponent getBaseAppComponent();
    }

    interface Presenter extends CustomerPresenter<View> {

    }
}