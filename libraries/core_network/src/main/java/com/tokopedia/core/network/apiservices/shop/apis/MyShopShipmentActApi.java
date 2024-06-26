package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public interface MyShopShipmentActApi {
    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_UPDATE_SHIPPING_INFO)
    Observable<Response<TkpdResponse>> updateShipmentInfo(@FieldMap Map<String, String> params);
}
