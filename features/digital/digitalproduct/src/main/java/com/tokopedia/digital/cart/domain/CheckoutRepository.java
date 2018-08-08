package com.tokopedia.digital.cart.domain;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.digital.cart.data.entity.response.ResponseInstantCheckoutData;
import com.tokopedia.digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.digital.cart.model.CheckoutDigitalData;
import com.tokopedia.digital.cart.model.InstantCheckoutData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class CheckoutRepository implements ICheckoutRepository {
    private final DigitalEndpointService digitalEndpointService;
    private final ICartMapperData cartMapperData;

    public CheckoutRepository(DigitalEndpointService digitalEndpointService,
                              ICartMapperData mapperData) {
        this.digitalEndpointService = digitalEndpointService;
        this.cartMapperData = mapperData;
    }

    @Override
    public Observable<CheckoutDigitalData> checkoutCart(RequestBodyCheckout requestBodyCheckout) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodyCheckout));
        JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalEndpointService.getApi().checkout(requestBody)
                .map(getFuncResponseToCheckoutDigitalData());
    }

    @Override
    public Observable<InstantCheckoutData> instantCheckoutCart(
            RequestBodyCheckout requestBodyCheckout
    ) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodyCheckout));
        JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalEndpointService.getApi().checkout( requestBody)
                .map(getFuncResponseToInstantCheckoutData());
    }

    @NonNull
    private Func1<Response<TkpdDigitalResponse>, CheckoutDigitalData>
    getFuncResponseToCheckoutDigitalData() {
        return new Func1<Response<TkpdDigitalResponse>, CheckoutDigitalData>() {
            @Override
            public CheckoutDigitalData call(
                    Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                return cartMapperData.transformCheckoutData(
                        tkpdDigitalResponseResponse.body()
                                .convertDataObj(ResponseCheckoutData.class)
                );
            }
        };
    }

    @NonNull
    private Func1<Response<TkpdDigitalResponse>, InstantCheckoutData>
    getFuncResponseToInstantCheckoutData() {
        return new Func1<Response<TkpdDigitalResponse>, InstantCheckoutData>() {
            @Override
            public InstantCheckoutData call(
                    Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                return cartMapperData.transformInstantCheckoutData(
                        tkpdDigitalResponseResponse.body()
                                .convertDataObj(ResponseInstantCheckoutData.class)
                );
            }
        };
    }
}
