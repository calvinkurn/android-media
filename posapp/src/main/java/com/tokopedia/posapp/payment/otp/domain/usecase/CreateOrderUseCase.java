package com.tokopedia.posapp.payment.otp.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.payment.otp.data.repository.PaymentRepository;
import com.tokopedia.posapp.payment.otp.domain.model.CreateOrderDomain;

import rx.Observable;

/**
 * Created by okasurya on 10/10/17.
 */

public class CreateOrderUseCase extends UseCase<CreateOrderDomain> {
    private PaymentRepository paymentRepository;

    public CreateOrderUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              PaymentRepository paymentRepository) {
        super(threadExecutor, postExecutionThread);
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Observable<CreateOrderDomain> createObservable(RequestParams requestParams) {
        return paymentRepository.createOrder(requestParams);
    }
}
