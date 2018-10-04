package com.tokopedia.affiliate.feature.onboarding.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by milhamj on 10/4/18.
 */
public interface UsernameInputContract {
    interface View extends CustomerView {

    }
    interface Presenter extends CustomerPresenter<View> {

    }
}
