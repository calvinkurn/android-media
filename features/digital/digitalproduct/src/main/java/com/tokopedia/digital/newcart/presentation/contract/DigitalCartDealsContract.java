package com.tokopedia.digital.newcart.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface DigitalCartDealsContract {

    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}
