package com.tokopedia.tkpd.network.apiservices.shop.apis;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface MyShopShipmentApi {
    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHIPPING_INFO)
    Observable<Response<TkpdResponse>> getShipmenttInfo(@FieldMap Map<String, String> params);
}
