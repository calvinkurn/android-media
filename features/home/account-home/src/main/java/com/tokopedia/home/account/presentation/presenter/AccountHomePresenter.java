package com.tokopedia.home.account.presentation.presenter;

import com.tokopedia.home.account.domain.GetAccountUseCase;
import com.tokopedia.home.account.presentation.AccountHome;
import com.tokopedia.home.account.presentation.viewmodel.AccountViewModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountHomePresenter implements AccountHome.Presenter {
    private GetAccountUseCase getAccountUseCase;

    @Inject
    public AccountHomePresenter(GetAccountUseCase getAccountUseCase) {
        this.getAccountUseCase = getAccountUseCase;
    }

    private AccountHome.View view;

    @Override
    public void getAccount(String query) {
        RequestParams requestParams = RequestParams.create();
        getAccountUseCase.execute(requestParams, new Subscriber<AccountViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(AccountViewModel accountViewModel) {

            }
        });

    }

    @Override
    public void attachView(AccountHome.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        getAccountUseCase.unsubscribe();
        view = null;
    }
}
