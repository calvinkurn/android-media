package com.tokopedia.common_digital.cart.data.datasource;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.common_digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.common_digital.cart.view.model.CheckoutDigitalData;
import com.tokopedia.common_digital.common.data.api.DigitalRestApi;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 28/08/18.
 */
public class DigitalCheckoutDataSource {

    private DigitalRestApi digitalRestApi;
    private ICartMapperData cartMapperData;

    public DigitalCheckoutDataSource(DigitalRestApi digitalRestApi, ICartMapperData cartMapperData) {
        this.digitalRestApi = digitalRestApi;
        this.cartMapperData = cartMapperData;
    }

    public Observable<CheckoutDigitalData> checkout(RequestBodyCheckout requestBodyCheckout) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodyCheckout));
        JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalRestApi.checkout(requestBody)
                .map(getFuncResponseToCheckoutDigitalData());
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

}
