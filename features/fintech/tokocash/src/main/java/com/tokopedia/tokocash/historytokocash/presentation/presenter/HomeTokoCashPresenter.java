package com.tokopedia.tokocash.historytokocash.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tokocash.balance.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.tokocash.historytokocash.domain.GetHistoryDataUseCase;
import com.tokopedia.tokocash.historytokocash.presentation.contract.HomeTokoCashContract;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;
import com.tokopedia.tokocash.network.exception.UserInactivateTokoCashException;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 2/6/18.
 */

public class HomeTokoCashPresenter extends BaseDaggerPresenter<HomeTokoCashContract.View>
        implements HomeTokoCashContract.Presenter {

    private GetBalanceTokoCashUseCase balanceTokoCashUseCase;
    private GetHistoryDataUseCase getHistoryDataUseCase;
    private UserSessionInterface userSession;

    @Inject
    public HomeTokoCashPresenter(GetBalanceTokoCashUseCase balanceTokoCashUseCase,
                                 GetHistoryDataUseCase getHistoryDataUseCase,
                                 UserSessionInterface userSession) {
        this.balanceTokoCashUseCase = balanceTokoCashUseCase;
        this.getHistoryDataUseCase = getHistoryDataUseCase;
        this.userSession = userSession;
    }

    //This method is just for refreshing token wallet when token is expired from applinks
    @Override
    public void getHistoryTokocashForRefreshingTokenWallet() {
        if (userSession.isLoggedIn()) {
            getView().showProgressLoading();
            RequestParams requestParams = RequestParams.create();
            requestParams.putString(GetHistoryDataUseCase.TYPE, "pending");
            requestParams.putString(GetHistoryDataUseCase.START_DATE, "");
            requestParams.putString(GetHistoryDataUseCase.END_DATE, "");
            requestParams.putString(GetHistoryDataUseCase.PAGE, "1");
            getHistoryDataUseCase.execute(requestParams,
                    new Subscriber<TokoCashHistoryData>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (isViewAttached()) {
                                if (e instanceof UserInactivateTokoCashException) {
                                    getView().navigatePageToActivateTokocash();
                                } else {
                                    getView().hideProgressLoading();
                                    getView().showEmptyPage(e);
                                }
                            }
                        }

                        @Override
                        public void onNext(TokoCashHistoryData tokoCashHistoryData) {
                            getView().hideProgressLoading();
                            processGetBalanceTokoCash();
                        }
                    });
        } else {
            getView().navigateToLoginPage();
        }
    }

    @Override
    public void processGetBalanceTokoCash() {
        getView().showProgressLoading();
        balanceTokoCashUseCase.execute(RequestParams.EMPTY, new Subscriber<BalanceTokoCash>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideProgressLoading();
                    getView().showEmptyPage(e);
                }
            }

            @Override
            public void onNext(BalanceTokoCash balanceTokoCash) {
                getView().hideProgressLoading();
                getView().renderBalanceTokoCash(balanceTokoCash);
            }
        });
    }

    @Override
    public void onDestroyView() {
        detachView();
        if (balanceTokoCashUseCase != null)
            balanceTokoCashUseCase.unsubscribe();
    }
}
