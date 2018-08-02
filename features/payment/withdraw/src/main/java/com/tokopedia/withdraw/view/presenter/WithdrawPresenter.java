package com.tokopedia.withdraw.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.withdraw.domain.model.InfoDepositDomainModel;
import com.tokopedia.withdraw.domain.usecase.DepositUseCase;
import com.tokopedia.withdraw.view.listener.WithdrawContract;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by StevenFredian on 30/07/18.
 */

public class WithdrawPresenter extends BaseDaggerPresenter<WithdrawContract.View>
        implements WithdrawContract.Presenter{

    private DepositUseCase depositUseCase;
    private UserSession userSession;

    @Inject
    public WithdrawPresenter(DepositUseCase depositUseCase, UserSession userSession){
        this.depositUseCase = depositUseCase;
        this.userSession = userSession;
    }

    @Override
    public void attachView(WithdrawContract.View view) {
        super.attachView(view);
    }

    @Override
    public void getWithdrawForm() {
        getView().showLoading();
        depositUseCase.execute(DepositUseCase.createParams(userSession), new Subscriber<InfoDepositDomainModel>() {
            @Override
            public void onCompleted() {
                getView().hideLoading();
            }

            @Override
            public void onError(Throwable throwable) {
                getView().hideLoading();
                getView().showError(throwable.toString());
            }

            @Override
            public void onNext(InfoDepositDomainModel infoDepositDomainModel) {
                getView().hideLoading();
                getView().onSuccessGetWithdrawForm(infoDepositDomainModel.getBankAccount());

            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
