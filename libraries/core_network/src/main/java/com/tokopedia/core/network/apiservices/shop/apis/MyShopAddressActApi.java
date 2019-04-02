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
public interface MyShopAddressActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_ADD_LOCATION)
    Observable<Response<TkpdResponse>> addLocation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_DELETE_LOCATION)
    Observable<Response<TkpdResponse>> deleteLocation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_NEW_ORDER_LOCATION)
    Observable<Response<TkpdResponse>> newOrderLocation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_EDIT_LOCATION)
    Observable<Response<TkpdResponse>> editLocation(@FieldMap Map<String, String> params);
}
