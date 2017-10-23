package com.tokopedia.posapp.data.mapper;

import android.util.Log;

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
        CreateOrderDomain createOrderDomain = new CreateOrderDomain();
        if(response.isSuccessful()
                && response.body() != null
                && response.body().getStatus().equals(SUCCESS)) {
            Log.d("o2o", response.body().getStrResponse());
            PaymentAction paymentAction = response.body().convertDataObj(PaymentAction.class);
            createOrderDomain.setOrderId(paymentAction.getOrderId());
            createOrderDomain.setInvoiceRef(paymentAction.getInvoiceRef());
            createOrderDomain.setStatus(true);
        } else {
            createOrderDomain.setStatus(false);
        }
        return createOrderDomain;
    }
}
