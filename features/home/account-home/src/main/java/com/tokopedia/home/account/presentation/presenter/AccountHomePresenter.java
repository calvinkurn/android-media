package com.tokopedia.home.account.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.home.account.domain.GetAccountUseCase;
import com.tokopedia.home.account.presentation.AccountHome;

import javax.inject.Inject;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountHomePresenter extends BaseDaggerPresenter<AccountHome.View> implements AccountHome.Presenter {

    private GetAccountUseCase getAccountUseCase;
    private AccountHome.View view;
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
