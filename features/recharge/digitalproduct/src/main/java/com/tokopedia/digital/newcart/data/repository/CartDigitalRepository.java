package com.tokopedia.digital.newcart.data.repository;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.digital.common.data.apiservice.DigitalRestApi;
import com.tokopedia.digital.newcart.data.entity.requestbody.otpcart.RequestBodyOtpSuccess;
import com.tokopedia.digital.newcart.data.entity.requestbody.voucher.RequestBodyCancelVoucher;
import com.tokopedia.digital.newcart.data.entity.response.ResponsePatchOtpSuccess;
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCartData;
import com.tokopedia.digital.newcart.domain.ICartDigitalRepository;
import com.tokopedia.digital.newcart.domain.mapper.ICartMapperData;
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData;
import com.tokopedia.network.data.model.response.DataResponse;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 3/2/17.
 */

public class CartDigitalRepository implements ICartDigitalRepository {

    private final DigitalRestApi digitalRestApi;
    private final ICartMapperData cartMapperData;

    public CartDigitalRepository(DigitalRestApi digitalRestApi,
                                 ICartMapperData iCartMapperData) {
        this.cartMapperData = iCartMapperData;
        this.digitalRestApi = digitalRestApi;
    }

    @Override
    public Observable<CartDigitalInfoData> getCartInfoData(Map<String, String> param) {
        return digitalRestApi.getCart(param)
                .map(getFuncResponseToCartDigitalInfoData());
    }

    @Override
    public Observable<CartDigitalInfoData> patchOtpCart(
            RequestBodyOtpSuccess requestBodyOtpSuccess,
            final Map<String, String> paramGetCart
    ) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodyOtpSuccess));
        JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalRestApi.patchOtpCart(requestBody)
                .flatMap(getFuncResponsePatchToGetCartInfo(paramGetCart));
    }

    @Override
    public Observable<Boolean> cancelVoucher(RequestBodyCancelVoucher requestBodyCancelVoucher) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodyCancelVoucher));
        JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);

        return digitalRestApi
                .cancelVoucher(requestBody)
                .map(cancelVoucherDataResponse ->
                        cartMapperData.transformCancelVoucherData(cancelVoucherDataResponse.body().getData()));
    }

    @NonNull
    private Func1<Response<DataResponse<ResponseCartData>>, CartDigitalInfoData>
    getFuncResponseToCartDigitalInfoData() {
        return new Func1<Response<DataResponse<ResponseCartData>>, CartDigitalInfoData>() {
            @Override
            public CartDigitalInfoData call(Response<DataResponse<ResponseCartData>> tkpdDigitalResponseResponse) {
                return cartMapperData.transformCartInfoData(
                        tkpdDigitalResponseResponse.body().getData()
                );
            }
        };
    }

    @NonNull
    private Func1<Response<TkpdDigitalResponse>, Observable<CartDigitalInfoData>>
    getFuncResponsePatchToGetCartInfo(final Map<String, String> paramGetCart) {
        return tkpdDigitalResponseResponse -> {
            if (tkpdDigitalResponseResponse.code() == 200) {
                ResponsePatchOtpSuccess responsePatchOtpSuccess =
                        tkpdDigitalResponseResponse.body().convertDataObj(
                                ResponsePatchOtpSuccess.class
                        );
                if (responsePatchOtpSuccess.isSuccess()) {
                    return digitalRestApi.getCart(paramGetCart)
                            .map(getFuncResponseToCartDigitalInfoData());
                }
            }
            throw new RuntimeException("Gagal COY!!!!!!");
        };
    }

}
