package com.tokopedia.digital.cart.domain;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.digital.cart.data.mapper.CartMapperData;
import com.tokopedia.digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.digital.cart.model.CheckoutDigitalData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class CheckoutRepository implements ICheckoutRepository {
    private final DigitalEndpointService digitalEndpointService;
    private final ICartMapperData cartMapperData;

    public CheckoutRepository() {
        this.digitalEndpointService = new DigitalEndpointService();
        this.cartMapperData = new CartMapperData();
    }

    @Override
    public Observable<CheckoutDigitalData> checkoutCart(RequestBodyCheckout requestBodyCheckout) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodyCheckout));
        JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalEndpointService.getApi().checkout(requestBody)
                .map(new Func1<Response<TkpdDigitalResponse>, CheckoutDigitalData>() {
                    @Override
                    public CheckoutDigitalData call(
                            Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return cartMapperData.transformCheckoutData(
                                tkpdDigitalResponseResponse.body()
                                        .convertDataObj(ResponseCheckoutData.class)
                        );
                    }
                });
    }
}
