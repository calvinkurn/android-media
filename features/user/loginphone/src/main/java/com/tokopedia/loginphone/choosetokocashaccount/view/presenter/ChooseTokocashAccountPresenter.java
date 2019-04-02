package com.tokopedia.loginphone.choosetokocashaccount.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.loginphone.choosetokocashaccount.domain.LoginPhoneNumberUseCase;
import com.tokopedia.loginphone.choosetokocashaccount.view.listener.ChooseTokocashAccountContract;
import com.tokopedia.loginphone.choosetokocashaccount.view.subscriber.LoginPhoneNumberSubscriber;
import com.tokopedia.sessioncommon.data.loginphone.UserDetail;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author by nisie on 12/4/17.
 */

public class ChooseTokocashAccountPresenter extends BaseDaggerPresenter<ChooseTokocashAccountContract.View>
        implements ChooseTokocashAccountContract.Presenter {

    private static final int ONLY_ONE = 1;
    private final LoginPhoneNumberUseCase loginTokoCashUseCase;
    private final UserSessionInterface userSessionInterface;

    @Inject
    public ChooseTokocashAccountPresenter(LoginPhoneNumberUseCase loginTokoCashUseCase,
                                          @Named(SessionModule.SESSION_MODULE) UserSessionInterface userSessionInterface) {
        this.loginTokoCashUseCase = loginTokoCashUseCase;
        this.userSessionInterface = userSessionInterface;
    }

    @Override
    public void detachView() {
        super.detachView();
        loginTokoCashUseCase.unsubscribe();
    }

    @Override
    public void loginWithTokocash(String key, UserDetail accountTokocash) {
        getView().showLoadingProgress();
        loginTokoCashUseCase.execute(LoginPhoneNumberUseCase.getParam(
                key,
                accountTokocash.getEmail(),
                String.valueOf(accountTokocash.getTkpdUserId()),
                userSessionInterface.getDeviceId()
        ), new LoginPhoneNumberSubscriber(getView().getContext(),
                getView().getLoginRouter(),
                "", getView()));
    }

    @Override
    public void checkAutoLogin(String key, int itemCount, List<UserDetail> list) {
        if (itemCount == ONLY_ONE) {
            UserDetail accountTokocash = list.get(0);
            loginWithTokocash(key, accountTokocash);
        }
    }

}
