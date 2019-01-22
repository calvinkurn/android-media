package com.tokopedia.common_digital.cart.data.datasource;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common_digital.cart.data.entity.requestbody.checkout.RequestBodyCheckout;
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData;
import com.tokopedia.common_digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.common_digital.common.data.api.DigitalRestApi;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 28/08/18.
 */
public class DigitalInstantCheckoutDataSource {

    private DigitalRestApi digitalRestApi;
    private ICartMapperData cartMapperData;

    public DigitalInstantCheckoutDataSource(DigitalRestApi digitalRestApi, ICartMapperData cartMapperData) {
        this.digitalRestApi = digitalRestApi;
        this.cartMapperData = cartMapperData;
    }

    public Observable<InstantCheckoutData> instantCheckout(RequestBodyCheckout requestBodyCheckout) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodyCheckout));
        JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalRestApi.checkout(requestBody)
                .map(getFuncResponseToCheckoutDigitalData());
    }

    @NonNull
    private Func1<Response<DataResponse<ResponseCheckoutData>>, InstantCheckoutData>
    getFuncResponseToCheckoutDigitalData() {
        return tkpdDigitalResponseResponse -> cartMapperData.transformInstantCheckoutData(
                tkpdDigitalResponseResponse.body().getData()
        );
    }

}
