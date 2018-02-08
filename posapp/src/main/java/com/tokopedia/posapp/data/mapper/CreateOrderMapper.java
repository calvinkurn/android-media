package com.tokopedia.posapp.data.mapper;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.data.pojo.base.GeneralResponse;
import com.tokopedia.posapp.data.pojo.payment.PaymentAction;
import com.tokopedia.posapp.domain.model.CreateOrderDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 10/11/17.
 */

public class CreateOrderMapper implements Func1<Response<TkpdResponse>, CreateOrderDomain> {

    private static final String SUCCESS = "SUCCESS";

    private Gson gson;

    public CreateOrderMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public CreateOrderDomain call(Response<TkpdResponse> response) {
        if(response.isSuccessful()
                && response.body() != null
                && response.body().getStatus().equals("200 Ok")) {
            Log.d("o2o", response.body().getStrResponse());
            GeneralResponse<PaymentAction> paymentAction = gson.fromJson(
                    response.body().getStringData(),
                    new TypeToken<GeneralResponse<PaymentAction>>(){}.getType()
            );

            CreateOrderDomain createOrderDomain = new CreateOrderDomain();
            if(paymentAction != null
                    && paymentAction.getData() != null) {
                createOrderDomain.setOrderId(paymentAction.getData().getOrderId());
                createOrderDomain.setInvoiceRef(paymentAction.getData().getInvoiceRef());
                createOrderDomain.setStatus(true);
                return createOrderDomain;
            }
        }

        throw new RuntimeException("Failed to create order");
    }
}
