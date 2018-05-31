package com.tokopedia.posapp.payment.otp.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.posapp.payment.PaymentConst;
import com.tokopedia.posapp.payment.otp.data.pojo.transaction.CheckTransactionResponse;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;
import com.tokopedia.posapp.payment.otp.exception.TransactionFailedException;
import com.tokopedia.posapp.payment.otp.exception.TransactionPendingException;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.posapp.payment.PaymentConst.TransactionStatus.ORDER_PROGRESS;
import static com.tokopedia.posapp.payment.PaymentConst.TransactionStatus.ORDER_SUCCESS;
import static com.tokopedia.posapp.payment.PaymentConst.TransactionStatus.PAYMENT_PROGRESS;
import static com.tokopedia.posapp.payment.PaymentConst.TransactionStatus.PAYMENT_SUCCESS;

/**
 * @author okasurya on 5/4/18.
 */

public class CheckTransactionMapper implements Func1<Response<DataResponse<CheckTransactionResponse>>, PaymentStatusDomain> {
    @Inject
    CheckTransactionMapper(){

    }

    @Override
    public PaymentStatusDomain call(Response<DataResponse<CheckTransactionResponse>> response) {
        switch (response.body().getData().getTransactionStatus()) {
            case ORDER_SUCCESS:
                return successResponse(response.body().getData());
            case PAYMENT_PROGRESS:
            case PAYMENT_SUCCESS:
            case ORDER_PROGRESS:
                throw new TransactionPendingException();
            default:
                throw new TransactionFailedException();
        }
    }

    private PaymentStatusDomain successResponse(CheckTransactionResponse data) {
        PaymentStatusDomain paymentStatusDomain = new PaymentStatusDomain();
        paymentStatusDomain.setOrderId(data.getOrderData().getOrderId());
        paymentStatusDomain.setInvoiceRef(data.getOrderData().getInvoiceRef());
        return paymentStatusDomain;
    }
}
