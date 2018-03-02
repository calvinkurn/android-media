package com.tokopedia.posapp.payment.otp.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.payment.otp.data.repository.PaymentRepository;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;

import rx.Observable;

/**
 * Created by okasurya on 10/10/17.
 */

public class CheckPaymentStatusUseCase extends UseCase<PaymentStatusDomain> {
    private PaymentRepository paymentRepository;

    public CheckPaymentStatusUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     PaymentRepository paymentRepository) {
        super(threadExecutor, postExecutionThread);
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Observable<PaymentStatusDomain> createObservable(RequestParams requestParams) {
        return paymentRepository.getPaymentStatus(requestParams);
    }
}
