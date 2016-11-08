package com.tokopedia.core.network.apiservices.etc.apis;

import com.tkpd.library.utils.data.model.ListShippingCity;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * AddressApi
 * Created by Angga.Prasetiyo on 07/12/2015.
 */
public interface AddressApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_CITY)
    Observable<Response<TkpdResponse>> getCity(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_DISTRICT)
    Observable<Response<TkpdResponse>> getDistrict(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_PROVINCE)
    Observable<Response<TkpdResponse>> getProvince(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_SHIPPING_CITY)
    Observable<Response<TkpdResponse>> getShippingCity(@FieldMap Map<String, String> params);

    @GET("/v4/address/"+TkpdBaseURL.Etc.PATH_GET_SHIPPING_CITY)
    Observable<Response<ListShippingCity>> getShippingCity(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Query(NetworkCalculator.USER_ID) String userId,// 5
            @Query(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Query(NetworkCalculator.HASH) String hash,// 7
            @Query(NetworkCalculator.DEVICE_TIME) String deviceTime
    );
}
