package com.tokopedia.posapp.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.data.pojo.payment.PaymentAction;
import com.tokopedia.posapp.domain.model.CreateOrderDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 10/11/17.
 */

public class CreateOrderMapper implements Func1<Response<TkpdResponse>, CreateOrderDomain> {

    private static final String SUCCESS = "SUCCESS";

    @Override
    public CreateOrderDomain call(Response<TkpdResponse> response) {
        if(response.isSuccessful()
                && response.body() != null
                && response.body().getStatus().equals(SUCCESS)) {
            PaymentAction paymentAction = response.body().convertDataObj(PaymentAction.class);
            CreateOrderDomain createOrderDomain = new CreateOrderDomain();
            createOrderDomain.setOrderId(paymentAction.getOrderId());
            return createOrderDomain;
        }
        return null;
    }
}
