package com.tokopedia.gamification.cracktoken.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.gamification.cracktoken.contract.CrackTokenContract;
import com.tokopedia.gamification.cracktoken.model.CrackResult;
import com.tokopedia.gamification.domain.GetCrackResultEggUseCase;
import com.tokopedia.gamification.domain.GetTokenTokopointsUseCase;
import com.tokopedia.gamification.floating.view.model.TokenData;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 4/2/18.
 */

public class CrackTokenPresenter extends BaseDaggerPresenter<CrackTokenContract.View>
        implements CrackTokenContract.Presenter {

    private GetTokenTokopointsUseCase getTokenTokopointsUseCase;
    private GetCrackResultEggUseCase getCrackResultEggUseCase;

    @Inject
    public CrackTokenPresenter(GetTokenTokopointsUseCase getTokenTokopointsUseCase, GetCrackResultEggUseCase getCrackResultEggUseCase) {
        this.getTokenTokopointsUseCase = getTokenTokopointsUseCase;
        this.getCrackResultEggUseCase = getCrackResultEggUseCase;
    }

    @Override
    public void crackToken(int tokenUserId, int campaignId) {
        getCrackResultEggUseCase.execute(getCrackResultEggUseCase.createRequestParam(tokenUserId, campaignId),
                new Subscriber<CrackResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onErrorCrackToken(e);
                    }

                    @Override
                    public void onNext(CrackResult crackResult) {
                        getView().onSuccessCrackToken(crackResult);
                    }
                });
    }

//    private void getGetTokenTokopoints(final CrackResult crackResult) {
//        getTokenTokopointsUseCase.execute(new Subscriber<TokenData>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                //TODO need to handle if error getting token but success getting crack egg?
//                getView().onErrorGetToken(e);
//            }
//
//            @Override
//            public void onNext(TokenData tokenData) {
//                getView().onSuccessCrackToken(crackResult);
//                getView().onSuccessGetToken(tokenData);
//            }
//        });
//    }

    public void getGetTokenTokopoints() {
        getTokenTokopointsUseCase.execute(new Subscriber<TokenData>() {
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
        getCrackResultEggUseCase.unsubscribe();
        getTokenTokopointsUseCase.unsubscribe();
    }
}
