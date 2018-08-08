package com.tokopedia.home.account.presentation.presenter;

import com.tokopedia.home.account.domain.GetAccountUseCase;
import com.tokopedia.home.account.presentation.AccountHome;
import com.tokopedia.home.account.presentation.viewmodel.base.AccountViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.home.account.AccountConstants.QUERY;
import static com.tokopedia.home.account.AccountConstants.VARIABLES;

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
    public void getAccount(String query) {
        view.showLoading();
        RequestParams requestParams = RequestParams.create();

        requestParams.putString(QUERY, query);
        requestParams.putObject(VARIABLES, new HashMap<>());

        getAccountUseCase.execute(requestParams, new Subscriber<AccountViewModel>() {
            @Override
            public void onCompleted() {
                view.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                view.showError(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(AccountViewModel accountViewModel) {
                view.renderData(accountViewModel);
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
