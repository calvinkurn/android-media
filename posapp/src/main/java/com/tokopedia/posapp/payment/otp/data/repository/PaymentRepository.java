package com.tokopedia.posapp.payment.otp.data.repository;

import com.tokopedia.posapp.payment.otp.domain.model.CreateOrderDomain;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public interface PaymentRepository {
    Observable<PaymentStatusDomain> getPaymentStatus(RequestParams requestParams);

    Observable<CreateOrderDomain> createOrder(RequestParams requestParams);
}
