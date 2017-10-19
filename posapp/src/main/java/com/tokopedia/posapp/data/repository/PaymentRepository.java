package com.tokopedia.posapp.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.posapp.domain.model.CreateOrderDomain;
import com.tokopedia.posapp.domain.model.payment.PaymentStatusDomain;

import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public interface PaymentRepository {
    Observable<PaymentStatusDomain> getPaymentStatus(RequestParams requestParams);

    Observable<CreateOrderDomain> createOrder(RequestParams requestParams);
}
