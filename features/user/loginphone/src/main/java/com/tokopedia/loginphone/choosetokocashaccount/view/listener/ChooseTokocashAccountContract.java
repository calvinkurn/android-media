package com.tokopedia.loginphone.choosetokocashaccount.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.loginphone.choosetokocashaccount.data.AccountTokocash;
import com.tokopedia.loginphone.choosetokocashaccount.data.LoginTokoCashViewModel;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;

import java.util.ArrayList;

/**
 * @author by nisie on 12/4/17.
 */

public interface ChooseTokocashAccountContract {
    interface View extends CustomerView {

        void onSuccessLogin();

        void showLoadingProgress();

        void dismissLoadingProgress();

        Context getContext();

        LoginSuccessRouter getLoginRouter();
    }

    public interface ViewAdapter {
        void onSelectedTokocashAccount(AccountTokocash accountTokocash);
    }

    interface Presenter extends CustomerPresenter<View> {

        void loginWithTokocash(String accessToken, AccountTokocash accountTokocash);

        void checkAutoLogin(String key, int itemCount, ArrayList<AccountTokocash> list);

    }
}
