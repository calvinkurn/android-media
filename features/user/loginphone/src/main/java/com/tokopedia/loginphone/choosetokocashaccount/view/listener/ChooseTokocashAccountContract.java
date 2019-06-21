package com.tokopedia.loginphone.choosetokocashaccount.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.loginphone.choosetokocashaccount.data.AccountList;
import com.tokopedia.loginphone.choosetokocashaccount.data.UserDetail;
import com.tokopedia.sessioncommon.view.LoginSuccessRouter;

/**
 * @author by nisie on 12/4/17.
 */

public interface ChooseTokocashAccountContract {
    interface View extends CustomerView {

        void onSuccessLogin(String userId);

        void showLoadingProgress();

        void dismissLoadingProgress();

        Context getContext();

        LoginSuccessRouter getLoginRouter();

        void onSuccessGetAccountList(AccountList accountList);

        void onErrorGetAccountList(Throwable e);
    }

    public interface ViewAdapter {
        void onSelectedTokocashAccount(UserDetail accountTokocash);
    }

    interface Presenter extends CustomerPresenter<View> {

        void loginWithTokocash(String key, UserDetail accountTokocash, String phoneNumber);

        void getAccountList(String validateToken, String phoneNumber);
    }
}
