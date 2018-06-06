package com.tokopedia.posapp.payment.otp.data.mapper;

import android.text.TextUtils;

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
        mockOrderData.setInvoiceRef("O2O/20180606/XVIII/VI/169529283");
        mockOrderData.setOrderId(169481131);

        mockTransactionData.setOrderData(mockOrderData);
        mockTransactionData.setTransactionStatus(ORDER_SUCCESS);
        mockTransactionData.setStatus("OK");

        mock.setData(mockTransactionData);
        mock.setMessage("200 Ok");
        return parseData(mock);
    }

    private PaymentStatusDomain mockFailedWithSuccessStatus() {
        PosResponse<CheckTransactionResponse> mock = new PosResponse<>();
        CheckTransactionResponse mockTransactionData = new CheckTransactionResponse();

        OrderDataResponse mockOrderData = new OrderDataResponse();
        mockOrderData.setInvoiceRef("");
        mockOrderData.setOrderId(null);

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
                return getResponse(posResponse.getData());
            case PAYMENT_PROGRESS:
            case PAYMENT_SUCCESS:
            case ORDER_PROGRESS:
                throw new TransactionPendingException();
            default:
                throw new TransactionFailedException();
        }
    }

    private PaymentStatusDomain getResponse(CheckTransactionResponse data) {
        if(isValid(data.getOrderData())) {
            PaymentStatusDomain paymentStatusDomain = new PaymentStatusDomain();
            paymentStatusDomain.setOrderId(data.getOrderData().getOrderId());
            paymentStatusDomain.setInvoiceRef(data.getOrderData().getInvoiceRef());
            return paymentStatusDomain;
        } else {
            throw new TransactionFailedException();
        }
    }

    private boolean isValid(OrderDataResponse orderData) {
        return !TextUtils.isEmpty(orderData.getInvoiceRef()) && orderData.getOrderId() != null && orderData.getOrderId() != 0;
    }
}
