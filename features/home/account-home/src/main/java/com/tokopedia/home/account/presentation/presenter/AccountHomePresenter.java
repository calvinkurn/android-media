package com.tokopedia.home.account.presentation.presenter;

import com.tokopedia.home.account.domain.GetAccountUseCase;
import com.tokopedia.home.account.presentation.AccountHome;

import javax.inject.Inject;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountHomePresenter implements AccountHome.Presenter {
    private GetAccountUseCase getAccountUseCase;
    private AccountHome.View view;

    @Inject
    public AccountHomePresenter(GetAccountUseCase getAccountUseCase) {
        this.getAccountUseCase = getAccountUseCase;
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
