package com.tokopedia.withdraw.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.withdraw.domain.model.WSErrorResponse;
import com.tokopedia.withdraw.domain.usecase.DoWithdrawUseCase;
import com.tokopedia.withdraw.view.listener.WithdrawPasswordContract;
import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by StevenFredian on 30/07/18.
 */

public class WithdrawPasswordPresenter extends BaseDaggerPresenter<WithdrawPasswordContract.View>
        implements WithdrawPasswordContract.Presenter{

    private UserSession userSession;
    private DoWithdrawUseCase doWithdrawUseCase;

    @Inject
    public WithdrawPasswordPresenter(DoWithdrawUseCase useCase, UserSession userSession){
        this.doWithdrawUseCase = useCase;
        this.userSession = userSession;
    }


    @Override
    public void attachView(WithdrawPasswordContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void doWithdraw(int withdrawal, BankAccountViewModel bankAccountViewModel, String password) {
        doWithdrawUseCase.execute(DoWithdrawUseCase.createParams
                        (userSession, withdrawal, bankAccountViewModel, password)
                , new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                ((WSErrorResponse.ErrorMessageException) throwable).getMessage();
            }

            @Override
            public void onNext(String s) {
                s.toString();
            }
        });
    }
}
