package com.tokopedia.loginphone.choosetokocashaccount.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.loginphone.choosetokocashaccount.data.AccountList;
import com.tokopedia.loginphone.choosetokocashaccount.data.UserDetail;
import com.tokopedia.loginphone.choosetokocashaccount.domain.GetAccountsListUseCase;
import com.tokopedia.loginphone.choosetokocashaccount.domain.LoginPhoneNumberUseCase;
import com.tokopedia.loginphone.choosetokocashaccount.view.listener.ChooseTokocashAccountContract;
import com.tokopedia.loginphone.choosetokocashaccount.view.subscriber.LoginPhoneNumberSubscriber;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

/**
 * @author by nisie on 12/4/17.
 */

public class ChooseTokocashAccountPresenter extends BaseDaggerPresenter<ChooseTokocashAccountContract.View>
        implements ChooseTokocashAccountContract.Presenter {

    private final GetAccountsListUseCase getAccountsListUseCase;
    private final LoginPhoneNumberUseCase loginTokoCashUseCase;
    private final UserSessionInterface userSessionInterface;

    @Inject
    public ChooseTokocashAccountPresenter(GetAccountsListUseCase getAccountsListUseCase,
            LoginPhoneNumberUseCase loginTokoCashUseCase,
                                          @Named(SessionModule.SESSION_MODULE) UserSessionInterface userSessionInterface) {
        this.getAccountsListUseCase = getAccountsListUseCase;
        this.loginTokoCashUseCase = loginTokoCashUseCase;
        this.userSessionInterface = userSessionInterface;
    }

    @Override
    public void detachView() {
        super.detachView();
        getAccountsListUseCase.unsubscribe();
        loginTokoCashUseCase.unsubscribe();
    }

    @Override
    public void loginWithTokocash(String key, UserDetail accountTokocash, String phoneNumber) {
        getView().showLoadingProgress();
        loginTokoCashUseCase.execute(LoginPhoneNumberUseCase.getParam(
                key,
                accountTokocash.getEmail(),
                String.valueOf(accountTokocash.getUserId()),
                userSessionInterface.getDeviceId(),
                phoneNumber
        ), new LoginPhoneNumberSubscriber(getView().getContext(),
                getView().getLoginRouter(),
                "", getView()));
    }

    @Override
    public void getAccountList(String validateToken, String phoneNumber) {
        getAccountsListUseCase.execute(GetAccountsListUseCase.Companion.getParam(validateToken,
            phoneNumber),
                new Subscriber<AccountList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorGetAccountList(e);
            }

            @Override
            public void onNext(AccountList accountList) {
                getView().onSuccessGetAccountList(accountList);
            }
        });
    }

}
