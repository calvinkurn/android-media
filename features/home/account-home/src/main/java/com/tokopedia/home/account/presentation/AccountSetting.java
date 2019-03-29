package com.tokopedia.home.account.presentation;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.account.data.model.AccountSettingConfig;
import com.tokopedia.home.account.presentation.listener.BaseAccountView;

/**
 * @author by nisie on 14/11/18.
 */
public interface AccountSetting {
    interface Presenter extends CustomerPresenter<View> {

        void getMenuAccountSetting();
    }

    interface View extends BaseAccountView {

        void onSuccessGetConfig(AccountSettingConfig accountSettingConfig);

        void logUnknownError(Throwable e);

        Context getContext();
    }
}
