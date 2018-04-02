package com.tokopedia.gamification.floating.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.gamification.domain.GetTokenTokopointsUseCase;
import com.tokopedia.gamification.floating.view.FloatingEggView;
import com.tokopedia.gamification.floatingtoken.model.TokenData;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hendry on 02/04/18.
 */

public class FloatingEggPresenter extends BaseDaggerPresenter<FloatingEggView> {
    private GetTokenTokopointsUseCase getTokenTokopointsUseCase;

    @Inject
    public FloatingEggPresenter(GetTokenTokopointsUseCase getTokenTokopointsUseCase) {
        this.getTokenTokopointsUseCase = getTokenTokopointsUseCase;
    }

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
    public void detachView() {
        super.detachView();
        getTokenTokopointsUseCase.unsubscribe();
    }
}