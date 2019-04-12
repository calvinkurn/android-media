package com.tokopedia.search.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface SearchContract {

    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}
