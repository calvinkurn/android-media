package com.tokopedia.affiliate.feature.onboarding.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by milhamj on 9/24/18.
 */
interface OnboardingContract {
    interface View extends CustomerView {

    }
    interface Presenter extends CustomerPresenter<View> {

    }
}
