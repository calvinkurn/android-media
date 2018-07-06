package com.tokopedia.posapp.payment.otp.data.source;

import com.tokopedia.posapp.payment.PaymentConst;
import com.tokopedia.posapp.payment.otp.data.mapper.CheckTransactionMapper;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 10/10/17.
 */

public class PaymentCloudSource {
    private PaymentApi paymentApi;
    private CheckTransactionMapper checkTransactionMapper;

    @Inject
    public PaymentCloudSource(PaymentApi paymentApi,
                              CheckTransactionMapper checkTransactionMapper) {
        this.paymentApi = paymentApi;
        this.checkTransactionMapper = checkTransactionMapper;
    }

    public Observable<PaymentStatusDomain> checkTransaction(RequestParams requestParams) {
        return paymentApi.checkTransaction(
                requestParams.getString(PaymentConst.Parameter.TRANSACTION_ID, "")
        ).map(checkTransactionMapper);
    }
}
