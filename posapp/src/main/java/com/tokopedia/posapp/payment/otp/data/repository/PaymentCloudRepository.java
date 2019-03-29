package com.tokopedia.posapp.payment.otp.data.repository;

import com.tokopedia.posapp.payment.otp.data.source.PaymentCloudSource;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 10/10/17.
 */

public class PaymentCloudRepository implements PaymentRepository {
    private PaymentCloudSource paymentCloudSource;

    @Inject
    public PaymentCloudRepository(PaymentCloudSource paymentCloudSource) {
        this.paymentCloudSource = paymentCloudSource;
    }

    @Override
    public Observable<PaymentStatusDomain> checkTransaction(RequestParams requestParams) {
        return paymentCloudSource.checkTransaction(requestParams);
    }
}
