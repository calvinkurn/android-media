package com.tokopedia.posapp.payment.otp.domain.usecase;

import com.tokopedia.posapp.payment.otp.data.repository.PaymentCloudRepository;
import com.tokopedia.posapp.payment.otp.data.repository.PaymentRepository;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 5/3/18.
 */

public class CheckTransactionUseCase extends UseCase<PaymentStatusDomain> {
    private PaymentRepository paymentRepository;

    @Inject
    CheckTransactionUseCase(PaymentCloudRepository paymentCloudRepository) {
        this.paymentRepository = paymentCloudRepository;
    }

    @Override
    public Observable<PaymentStatusDomain> createObservable(RequestParams requestParams) {
        return paymentRepository.checkTransaction(requestParams);
    }
}
