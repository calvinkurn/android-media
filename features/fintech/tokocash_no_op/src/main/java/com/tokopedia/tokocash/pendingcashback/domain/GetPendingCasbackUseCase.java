package com.tokopedia.tokocash.pendingcashback.domain;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/7/18.
 */

public class GetPendingCasbackUseCase extends UseCase<PendingCashback> {

    public GetPendingCasbackUseCase() {
    }

    @Override
    public Observable<PendingCashback> createObservable(RequestParams requestParams) {
        return null;
    }
}
