package com.tokopedia.withdraw.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.withdraw.R;
import com.tokopedia.withdraw.domain.model.InfoDepositDomainModel;
import com.tokopedia.withdraw.domain.usecase.DepositUseCase;
import com.tokopedia.withdraw.view.listener.WithdrawContract;
import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

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
    public void doWithdraw(String totalBalance, String totalWithdrawal, BankAccountViewModel selectedBank) {
        getView().resetView();
        int balance = (int) StringUtils.convertToNumeric(totalBalance,false);
        int withdrawal = (int) StringUtils.convertToNumeric(totalWithdrawal,false);
        if(balance < withdrawal){
            getView().showErrorWithdrawal(getView().getStringResource(R.string.error_withdraw_exceed_balance));
            return;
        }

        if(!(withdrawal > 0)){
            getView().showErrorWithdrawal(getView().getStringResource(R.string.error_field_required));
            return;
        }


        if(selectedBank == null){
            getView().showError(getView().getStringResource(R.string.has_no_bank));
            return;
        }

        getView().showConfirmPassword();
    }

    @Override
    public void detachView() {
        depositUseCase.unsubscribe();
        super.detachView();
    }
}
