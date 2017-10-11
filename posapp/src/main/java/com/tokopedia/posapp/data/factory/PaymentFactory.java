package com.tokopedia.posapp.data.factory;

import com.tokopedia.posapp.data.mapper.CreateOrderMapper;
import com.tokopedia.posapp.data.mapper.PaymentStatusMapper;
import com.tokopedia.posapp.data.source.cloud.PaymentCloudSource;
import com.tokopedia.posapp.data.source.cloud.api.PaymentApi;
import com.tokopedia.posapp.data.source.cloud.api.ScroogeApi;

/**
 * Created by okasurya on 10/10/17.
 */

public class PaymentFactory {

    private ScroogeApi scroogeApi;
    private PaymentApi paymentApi;
    private PaymentStatusMapper paymentStatusMapper;
    private CreateOrderMapper createOrderMapper;

    public PaymentFactory(ScroogeApi scroogeApi,
                          PaymentApi paymentApi,
                          PaymentStatusMapper paymentStatusMapper,
                          CreateOrderMapper createOrderMapper) {
        this.scroogeApi = scroogeApi;
        this.paymentApi = paymentApi;
        this.paymentStatusMapper = paymentStatusMapper;
        this.createOrderMapper = createOrderMapper;
    }

    public PaymentCloudSource cloud() {
        return new PaymentCloudSource(scroogeApi, paymentApi, paymentStatusMapper, createOrderMapper);
    }
}
