package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface SearchSectionContract {

    interface View extends CustomerView {

    }

    interface Presenter<T extends View> extends CustomerPresenter<T> {

    }
}
