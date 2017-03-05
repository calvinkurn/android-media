package com.tokopedia.digital.cart.domain;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.cart.data.entity.requestbody.atc.RequestBodyAtcDigital;
import com.tokopedia.digital.cart.data.entity.response.ResponseCartData;
import com.tokopedia.digital.cart.data.mapper.CartMapperData;
import com.tokopedia.digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.digital.cart.model.CartDigitalInfoData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class CartDigitalRepository implements ICartDigitalRepository {

    private final DigitalEndpointService digitalEndpointService;
    private final ICartMapperData cartMapperData;

    public CartDigitalRepository() {
        cartMapperData = new CartMapperData();
        digitalEndpointService = new DigitalEndpointService();
    }

    @Override
    public Observable<CartDigitalInfoData> getCartInfoData(TKPDMapParam<String, String> param) {
        return digitalEndpointService.getApi().getCart(param)
                .map(new Func1<Response<TkpdDigitalResponse>, CartDigitalInfoData>() {
                    @Override
                    public CartDigitalInfoData call(Response<TkpdDigitalResponse>
                                                            tkpdDigitalResponseResponse) {
                        return cartMapperData.transformCartInfoData(
                                tkpdDigitalResponseResponse.body().convertDataObj(
                                        ResponseCartData.class
                                )
                        );
                    }
                });
    }

    @Override
    public Observable<?> deleteCartData(TKPDMapParam<String, String> param) {
        return null;
    }

    @Override
    public Observable<CartDigitalInfoData> addToCart(
            RequestBodyAtcDigital requestBodyAtcDigital, String idemPotencyKeyHeader
    ) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodyAtcDigital));
        JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalEndpointService.getApi()
                .addToCart(requestBody, idemPotencyKeyHeader)
                .map(new Func1<Response<TkpdDigitalResponse>, CartDigitalInfoData>() {
                    @Override
                    public CartDigitalInfoData call(Response<TkpdDigitalResponse>
                                                            tkpdDigitalResponseResponse) {
                        return cartMapperData.transformCartInfoData(
                                tkpdDigitalResponseResponse.body().convertDataObj(
                                        ResponseCartData.class
                                )
                        );
                    }
                });
    }
}
