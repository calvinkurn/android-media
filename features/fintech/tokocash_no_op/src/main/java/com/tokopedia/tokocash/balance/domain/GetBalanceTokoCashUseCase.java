package com.tokopedia.tokocash.balance.domain;

import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class GetBalanceTokoCashUseCase extends UseCase<BalanceTokoCash> {


    public GetBalanceTokoCashUseCase() {
    }

    @Override
    public Observable<BalanceTokoCash> createObservable(RequestParams requestParams) {
        return null;
    }
}
