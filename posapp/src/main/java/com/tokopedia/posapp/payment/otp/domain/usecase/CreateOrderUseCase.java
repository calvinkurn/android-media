package com.tokopedia.posapp.payment.otp.domain.usecase;

import com.tokopedia.posapp.payment.otp.data.repository.PaymentCloudRepository;
import com.tokopedia.posapp.payment.otp.data.repository.PaymentRepository;
import com.tokopedia.posapp.payment.otp.domain.model.CreateOrderDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 10/10/17.
 */

public class CreateOrderUseCase extends UseCase<CreateOrderDomain> {
    private PaymentRepository paymentRepository;

    @Inject
    public CreateOrderUseCase(PaymentCloudRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Observable<CreateOrderDomain> createObservable(RequestParams requestParams) {
        return paymentRepository.createOrder(requestParams);
    }
}
