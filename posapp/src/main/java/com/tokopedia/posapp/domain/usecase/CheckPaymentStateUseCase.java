package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.posapp.domain.model.payment.PaymentStateDomain;

import rx.Observable;

/**
 * Created by okasurya on 10/10/17.
 */

public class CheckPaymentStateUseCase extends UseCase<PaymentStateDomain> {

    @Override
    public Observable<PaymentStateDomain> createObservable(RequestParams requestParams) {
        return null;
    }
}
