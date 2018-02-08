package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.PosConstants;
import com.tokopedia.posapp.data.factory.PaymentFactory;
import com.tokopedia.posapp.domain.model.CreateOrderDomain;
import com.tokopedia.posapp.domain.model.payment.PaymentStatusDomain;

import rx.Observable;

/**
 * Created by okasurya on 10/10/17.
 */

public class PaymentRepositoryImpl implements PaymentRepository {
    PaymentFactory paymentFactory;

    public PaymentRepositoryImpl(PaymentFactory paymentFactory) {
        this.paymentFactory = paymentFactory;
    }

    @Override
    public Observable<PaymentStatusDomain> getPaymentStatus(RequestParams requestParams) {
        return paymentFactory.cloud().getPaymentStatus(requestParams);
    }

    @Override
    public Observable<CreateOrderDomain> createOrder(RequestParams requestParams) {
        return paymentFactory.cloud().createOrder(requestParams);
    }
}
