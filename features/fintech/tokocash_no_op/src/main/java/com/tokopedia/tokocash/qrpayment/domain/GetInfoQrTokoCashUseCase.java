package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public class GetInfoQrTokoCashUseCase extends UseCase<InfoQrTokoCash> {

    public GetInfoQrTokoCashUseCase() {

    }

    @Override
    public Observable<InfoQrTokoCash> createObservable(RequestParams requestParams) {
        return null;
    }
}
