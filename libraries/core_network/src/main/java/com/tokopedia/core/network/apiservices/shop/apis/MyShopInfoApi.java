package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.myproduct.model.MyShopInfoModel;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public interface MyShopInfoApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_INFO)
    Observable<Response<TkpdResponse>> getInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_INFO)
    Observable<MyShopInfoModel> getInfo2(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST()
    Observable<MyShopInfoModel> getInfoBasic(
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Field("user_id") String userId,// 5
            @Field("device_id") String deviceId, // 6
            @Field("hash") String hash,// 7
            @Field("device_time") String deviceTime// 8
    );

}
