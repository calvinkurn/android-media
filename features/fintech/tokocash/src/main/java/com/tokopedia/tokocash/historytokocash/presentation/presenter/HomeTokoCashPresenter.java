package com.tokopedia.tokocash.historytokocash.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tokocash.historytokocash.presentation.contract.HomeTokoCashContract;
import com.tokopedia.tokocash.network.exception.UserInactivateTokoCashException;
import com.tokopedia.tokocash.qrpayment.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 2/6/18.
 */

public class HomeTokoCashPresenter extends BaseDaggerPresenter<HomeTokoCashContract.View>
        implements HomeTokoCashContract.Presenter {

    private GetBalanceTokoCashUseCase balanceTokoCashUseCase;

    @Inject
    public HomeTokoCashPresenter(GetBalanceTokoCashUseCase balanceTokoCashUseCase) {
        this.balanceTokoCashUseCase = balanceTokoCashUseCase;
    }

    @Override
    public void processGetCategoryTopUp() {

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
}
