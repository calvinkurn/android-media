package com.tokopedia.posapp.payment.otp.domain.usecase;

import com.tokopedia.posapp.payment.otp.data.repository.PaymentCloudRepository;
import com.tokopedia.posapp.payment.otp.data.repository.PaymentRepository;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 10/10/17.
 */

public class CheckPaymentStatusUseCase extends UseCase<PaymentStatusDomain> {
    private PaymentRepository paymentRepository;

    @Inject
    public CheckPaymentStatusUseCase(PaymentCloudRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Observable<PaymentStatusDomain> createObservable(RequestParams requestParams) {
        return paymentRepository.getPaymentStatus(requestParams);
    }
}
