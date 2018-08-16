package com.tokopedia.tokocash.historytokocash.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tokocash.historytokocash.domain.GetHistoryDataUseCase;
import com.tokopedia.tokocash.historytokocash.presentation.contract.HomeTokoCashContract;
import com.tokopedia.tokocash.historytokocash.presentation.model.TokoCashHistoryData;
import com.tokopedia.tokocash.network.exception.UserInactivateTokoCashException;
import com.tokopedia.tokocash.balance.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 2/6/18.
 */

public class HomeTokoCashPresenter extends BaseDaggerPresenter<HomeTokoCashContract.View>
        implements HomeTokoCashContract.Presenter {

    private GetBalanceTokoCashUseCase balanceTokoCashUseCase;
    private GetHistoryDataUseCase getHistoryDataUseCase;

    @Inject
    public HomeTokoCashPresenter(GetBalanceTokoCashUseCase balanceTokoCashUseCase, GetHistoryDataUseCase getHistoryDataUseCase) {
        this.balanceTokoCashUseCase = balanceTokoCashUseCase;
        this.getHistoryDataUseCase = getHistoryDataUseCase;
    }

    //This method is just for refreshing token wallet when token is expired from applinks
    @Override
    public void getHistoryTokocashForRefreshingTokenWallet() {
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
                        getView().showErrorMessage();
                    }

                    @Override
                    public void onNext(TokoCashHistoryData tokoCashHistoryData) {

                    }
                });
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
                getView().hideProgressLoading();
                e.printStackTrace();
                if (e instanceof UserInactivateTokoCashException) {
                    getView().showErrorMessage();
                } else {
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
        if (balanceTokoCashUseCase != null)
            balanceTokoCashUseCase.unsubscribe();
    }
}
