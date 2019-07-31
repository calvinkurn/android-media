package com.tokopedia.loginphone.choosetokocashaccount.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.loginphone.choosetokocashaccount.data.AccountList;
import com.tokopedia.loginphone.choosetokocashaccount.data.UserDetail;
import com.tokopedia.sessioncommon.data.LoginTokenPojo;
import com.tokopedia.sessioncommon.data.profile.ProfilePojo;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

/**
 * @author by nisie on 12/4/17.
 */

public interface ChooseTokocashAccountContract {
    interface View extends CustomerView {

        void onSuccessLogin(String userId);

        void showLoadingProgress();

        void dismissLoadingProgress();

        Context getContext();

        void onSuccessGetAccountList(AccountList accountList);

        void onErrorGetAccountList(Throwable e);

        @NotNull
        Function1<LoginTokenPojo, Unit> onSuccessLoginToken();

        @NotNull
        Function1<Throwable, Unit> onErrorLoginToken();

        @NotNull
        Function1<ProfilePojo, Unit> onSuccessGetUserInfo();

        @NotNull
        Function1<Throwable, Unit> onErrorGetUserInfo();

        @NotNull
        Function1<MessageErrorException, Unit> onGoToActivationPage();

        @NotNull
        Function0<Unit> onGoToSecurityQuestion();

        @NotNull
        Function2<String, String, Unit> onGoToCreatePassword();

    }

    public interface ViewAdapter {
        void onSelectedTokocashAccount(UserDetail accountTokocash);
    }

    interface Presenter extends CustomerPresenter<View> {

        void loginWithTokocash(String key, UserDetail accountTokocash, String phoneNumber);

        void getAccountList(String validateToken, String phoneNumber);

        void getUserInfo();
    }
}
