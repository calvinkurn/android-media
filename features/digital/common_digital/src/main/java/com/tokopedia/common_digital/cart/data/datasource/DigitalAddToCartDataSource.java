package com.tokopedia.common_digital.cart.data.datasource;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.common_digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCartData;
import com.tokopedia.common_digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.common_digital.cart.view.model.CartDigitalInfoData;
import com.tokopedia.common_digital.common.data.api.DigitalRestApi;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 24/08/18.
 */
public class DigitalAddToCartDataSource {

    private DigitalRestApi digitalRestApi;
    private ICartMapperData cartMapperData;

    public DigitalAddToCartDataSource(DigitalRestApi digitalRestApi, ICartMapperData cartMapperData) {
        this.digitalRestApi = digitalRestApi;
        this.cartMapperData = cartMapperData;
    }

    public Observable<CartDigitalInfoData> addToCart(
            RequestBodyAtcDigital requestBodyAtcDigital, String idemPotencyKeyHeader
    ) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodyAtcDigital));
        JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalRestApi.addToCart(requestBody, idemPotencyKeyHeader).map(getFuncResponseToCartDigitalInfoData());
    }

    @NonNull
    private Func1<Response<TkpdDigitalResponse>, CartDigitalInfoData>
    getFuncResponseToCartDigitalInfoData() {
        return tkpdDigitalResponseResponse -> cartMapperData.transformCartInfoData(
                tkpdDigitalResponseResponse.body().convertDataObj(
                        ResponseCartData.class
                )
        );
    }

}
