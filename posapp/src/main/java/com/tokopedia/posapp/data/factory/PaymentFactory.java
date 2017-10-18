package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.mapper.CreateOrderMapper;
import com.tokopedia.posapp.data.mapper.PaymentStatusMapper;
import com.tokopedia.posapp.data.source.cloud.PaymentCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.GatewayPaymentApi;
import com.tokopedia.posapp.data.source.cloud.api.ScroogeApi;

/**
 * Created by okasurya on 10/10/17.
 */

public class PaymentFactory {

    private GatewayPaymentApi gatewayPaymentApi;
    private ScroogeApi scroogeApi;
    private PaymentStatusMapper paymentStatusMapper;
    private CreateOrderMapper createOrderMapper;

    public PaymentFactory(GatewayPaymentApi gatewayPaymentApi,
                          ScroogeApi scroogeApi,
                          PaymentStatusMapper paymentStatusMapper,
                          CreateOrderMapper createOrderMapper) {
        this.gatewayPaymentApi = gatewayPaymentApi;
        this.scroogeApi = scroogeApi;
        this.paymentStatusMapper = paymentStatusMapper;
        this.createOrderMapper = createOrderMapper;
    }

    public PaymentCloudSource cloud() {
        return new PaymentCloudSource(gatewayPaymentApi, scroogeApi, paymentStatusMapper, createOrderMapper);
    }
}
