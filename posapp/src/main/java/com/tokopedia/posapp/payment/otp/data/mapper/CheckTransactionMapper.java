package com.tokopedia.posapp.payment.otp.data.mapper;

import com.tokopedia.posapp.base.data.pojo.PosResponse;
import com.tokopedia.posapp.payment.otp.data.pojo.transaction.CheckTransactionResponse;
import com.tokopedia.posapp.payment.otp.data.pojo.transaction.OrderDataResponse;
import com.tokopedia.posapp.payment.otp.domain.model.PaymentStatusDomain;
import com.tokopedia.posapp.payment.otp.exception.TransactionFailedException;
import com.tokopedia.posapp.payment.otp.exception.TransactionPendingException;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.posapp.payment.PaymentConst.TransactionStatus.ORDER_FAILED;
import static com.tokopedia.posapp.payment.PaymentConst.TransactionStatus.ORDER_PROGRESS;
import static com.tokopedia.posapp.payment.PaymentConst.TransactionStatus.ORDER_SUCCESS;
import static com.tokopedia.posapp.payment.PaymentConst.TransactionStatus.PAYMENT_PROGRESS;
import static com.tokopedia.posapp.payment.PaymentConst.TransactionStatus.PAYMENT_SUCCESS;

/**
 * @author okasurya on 5/4/18.
 */

public class CheckTransactionMapper implements Func1<Response<PosResponse<CheckTransactionResponse>>, PaymentStatusDomain> {
    @Inject
    CheckTransactionMapper(){

    }

    @Override
    public PaymentStatusDomain call(Response<PosResponse<CheckTransactionResponse>> response) {
        return parseData(response.body());
    }

    private PaymentStatusDomain mockSuccess() {
        PosResponse<CheckTransactionResponse> mock = new PosResponse<>();
        CheckTransactionResponse mockTransactionData = new CheckTransactionResponse();

        OrderDataResponse mockOrderData = new OrderDataResponse();
        mockOrderData.setInvoiceRef("INV/SOmething/SOMETHING");
        mockOrderData.setOrderId(123456);

        mockTransactionData.setOrderData(mockOrderData);
        mockTransactionData.setTransactionStatus(ORDER_SUCCESS);

        mock.setData(mockTransactionData);
        mock.setMessage("Success");
        return parseData(mock);
    }

    private PaymentStatusDomain mockFailed() {
        PosResponse<CheckTransactionResponse> mock = new PosResponse<>();
        CheckTransactionResponse mockTransactionData = new CheckTransactionResponse();

        mockTransactionData.setTransactionStatus(ORDER_FAILED);

        mock.setData(mockTransactionData);
        mock.setMessage("Failed");
        return parseData(mock);
    }

    private PaymentStatusDomain mockPending() {
        PosResponse<CheckTransactionResponse> mock = new PosResponse<>();
        CheckTransactionResponse mockTransactionData = new CheckTransactionResponse();

        mockTransactionData.setTransactionStatus(PAYMENT_PROGRESS);

        mock.setData(mockTransactionData);
        mock.setMessage("Pending");
        return parseData(mock);
    }

    private PaymentStatusDomain parseData(PosResponse<CheckTransactionResponse> posResponse) {
        switch (posResponse.getData().getTransactionStatus()) {
            case ORDER_SUCCESS:
                return successResponse(posResponse.getData());
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
