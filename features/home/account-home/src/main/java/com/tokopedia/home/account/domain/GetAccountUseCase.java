package com.tokopedia.home.account.domain;

import com.tokopedia.home.account.presentation.viewmodel.base.AccountViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 7/20/18.
 */
public class GetAccountUseCase extends UseCase<AccountViewModel> {

    @Inject
    GetAccountUseCase(){

    }

    @Override
    public Observable<AccountViewModel> createObservable(RequestParams requestParams) {
        return null;
    }
}
