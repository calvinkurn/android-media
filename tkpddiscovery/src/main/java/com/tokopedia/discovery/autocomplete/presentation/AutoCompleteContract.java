package com.tokopedia.discovery.autocomplete.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface AutoCompleteContract {
    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<AutoCompleteContract.View> {

    }
}
