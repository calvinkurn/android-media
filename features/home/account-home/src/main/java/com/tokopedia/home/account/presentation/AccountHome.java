package com.tokopedia.home.account.presentation;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.account.presentation.listener.BaseAccountView;

/**
 * @author okasurya on 7/20/18.
 */
public interface AccountHome {
    interface Presenter extends CustomerPresenter<View> {
        void sendUserAttributeTracker();
    }

    interface View extends BaseAccountView {
        Context getContext();
    }
}
