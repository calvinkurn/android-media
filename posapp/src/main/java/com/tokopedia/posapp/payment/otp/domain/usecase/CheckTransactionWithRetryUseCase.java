package com.tokopedia.posapp.payment.otp.domain.usecase;

import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;
import com.tokopedia.posapp.payment.otp.exception.TransactionFailedException;
import com.tokopedia.posapp.payment.otp.exception.TransactionPendingException;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author okasurya on 5/4/18.
 */

public class CheckTransactionWithRetryUseCase extends UseCase<PaymentStatusDomain> {
    private static final int COUNTER_START = 0;
    private static final int RETRY_AMOUNT = 4;
    private static final int RETRY_DELAY = 5; // in second
    private CheckTransactionUseCase checkTransactionUseCase;

    @Inject
    CheckTransactionWithRetryUseCase(CheckTransactionUseCase checkTransactionUseCase) {
        this.checkTransactionUseCase = checkTransactionUseCase;
    }

    @Override
    public Observable<PaymentStatusDomain> createObservable(final RequestParams requestParams) {
        return Observable.defer(
            new Func0<Observable<PaymentStatusDomain>>() {
                @Override
                public Observable<PaymentStatusDomain> call() {
                    return checkTransactionUseCase.createObservable(requestParams);
                }
            }
        ).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> attempts) {
                return attempts.zipWith(
                    Observable.range(COUNTER_START, RETRY_AMOUNT),
                    new Func2<Throwable, Integer, Integer>() {
                        @Override
                        public Integer call(Throwable throwable, Integer count) {
                            if (throwable instanceof TransactionPendingException) {
                                return count;
                            } else throw new RuntimeException(throwable);
                        }
                    }
                ).flatMap(
                    new Func1<Integer, Observable<?>>() {
                        @Override
                        public Observable<?> call(Integer i) {
                            if (i == RETRY_AMOUNT - 1) return Observable.error(new TransactionFailedException());

                            return Observable.timer(RETRY_DELAY, TimeUnit.SECONDS);
                        }
                    }
                );
            }
        });
    }
}
