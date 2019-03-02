package com.tokopedia.gamification.floating.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.gamification.domain.GetTokenTokopointsUseCase;
import com.tokopedia.gamification.floating.view.contract.FloatingEggContract;
import com.tokopedia.gamification.floating.view.model.TokenData;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hendry on 02/04/18.
 */

public class FloatingEggPresenter extends BaseDaggerPresenter<FloatingEggContract.View>
        implements FloatingEggContract.Presenter {

    private GetTokenTokopointsUseCase getTokenTokopointsUseCase;
    private UserSessionInterface userSession;

    @Inject
    public FloatingEggPresenter(GetTokenTokopointsUseCase getTokenTokopointsUseCase,
                                UserSessionInterface userSession) {
        this.getTokenTokopointsUseCase = getTokenTokopointsUseCase;
        this.userSession = userSession;
    }

    @Override
    public void getGetTokenTokopoints() {
        getTokenTokopointsUseCase.execute(null, new Subscriber<TokenData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorGetToken(e);
            }

            @Override
            public void onNext(TokenData tokenData) {
                getView().onSuccessGetToken(tokenData);
            }
        });
    }

    @Override
    public boolean isUserLogin() {
        return userSession.isLoggedIn();
    }

    @Override
    public void detachView() {
        super.detachView();
        getTokenTokopointsUseCase.unsubscribe();
    }
}