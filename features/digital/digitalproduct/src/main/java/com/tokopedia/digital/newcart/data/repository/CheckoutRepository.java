package com.tokopedia.digital.newcart.data.repository;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.digital.common.data.apiservice.DigitalRestApi;
import com.tokopedia.digital.newcart.data.mapper.ICartMapperData;
import com.tokopedia.digital.newcart.domain.ICheckoutRepository;
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData;

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
                .map(new Func1<Response<DataResponse<com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData>>, CheckoutDigitalData>() {
                    @Override
                    public CheckoutDigitalData call(Response<DataResponse<com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData>> dataResponseResponse) {
                        return cartMapperData.transformCheckoutData(dataResponseResponse.body().getData());
                    }
                });
    }

}
