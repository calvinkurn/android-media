package com.tokopedia.posapp.payment.otp.data.factory;

import com.tokopedia.posapp.payment.otp.data.source.PaymentApi;
import com.tokopedia.posapp.payment.otp.data.mapper.CreateOrderMapper;
import com.tokopedia.posapp.payment.otp.data.mapper.PaymentStatusMapper;
import com.tokopedia.posapp.payment.otp.data.source.PaymentCloudSource;

/**
 * Created by okasurya on 10/10/17.
 */
@Deprecated
public class PaymentFactory {

    private PaymentApi paymentApi;
    private PaymentStatusMapper paymentStatusMapper;
    private CreateOrderMapper createOrderMapper;

    public PaymentFactory(PaymentApi paymentApi,
                          PaymentStatusMapper paymentStatusMapper,
                          CreateOrderMapper createOrderMapper) {
        this.paymentApi = paymentApi;
        this.paymentStatusMapper = paymentStatusMapper;
        this.createOrderMapper = createOrderMapper;
    }

    public PaymentCloudSource cloud() {
        return new PaymentCloudSource(paymentApi, paymentStatusMapper, createOrderMapper);
    }
}
