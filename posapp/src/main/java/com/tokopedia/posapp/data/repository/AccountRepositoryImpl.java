package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.data.factory.AccountFactory;
import com.tokopedia.posapp.domain.model.CheckPasswordDomain;

import rx.Observable;

/**
 * Created by okasurya on 9/28/17.
 */

public class AccountRepositoryImpl implements AccountRepository {
    private AccountFactory accountFactory;

    public AccountRepositoryImpl(AccountFactory accountFactory) {
        this.accountFactory = accountFactory;
    }

    @Override
    public Observable<CheckPasswordDomain> validatePassword(RequestParams requestParams) {
        return accountFactory.cloud().validatePassword(requestParams);
    }
}
