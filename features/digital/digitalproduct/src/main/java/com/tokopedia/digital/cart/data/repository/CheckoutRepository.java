package com.tokopedia.digital.cart.data.repository;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.digital.cart.data.entity.response.checkout.ResponseCheckoutData;
import com.tokopedia.digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.digital.cart.domain.ICheckoutRepository;
import com.tokopedia.digital.cart.presentation.model.CheckoutDigitalData;
import com.tokopedia.digital.common.data.apiservice.DigitalRestApi;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class CheckoutRepository implements ICheckoutRepository {

    private final DigitalRestApi digitalRestApi;
    private final ICartMapperData cartMapperData;

    public CheckoutRepository(DigitalRestApi digitalRestApi,
                              ICartMapperData mapperData) {
        this.digitalRestApi = digitalRestApi;
        this.cartMapperData = mapperData;
    }

    @Override
    public Observable<CheckoutDigitalData> checkoutCart(RequestBodyCheckout requestBodyCheckout) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodyCheckout));
        JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalRestApi.checkout(requestBody)
                .map(getFuncResponseToCheckoutDigitalData());
    }

    @NonNull
    private Func1<Response<TkpdDigitalResponse>, CheckoutDigitalData>
    getFuncResponseToCheckoutDigitalData() {
        return tkpdDigitalResponseResponse -> cartMapperData.transformCheckoutData(
                tkpdDigitalResponseResponse.body()
                        .convertDataObj(ResponseCheckoutData.class)
        );
    }

}
