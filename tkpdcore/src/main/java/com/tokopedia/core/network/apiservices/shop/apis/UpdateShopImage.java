package com.tokopedia.core.network.apiservices.shop.apis;

import com.tokopedia.core.shop.model.UpdateShopImageModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author Toped10 on 5/26/2016.
 */
public interface UpdateShopImage {
    @FormUrlEncoded
    @POST("")
    Observable<UpdateShopImageModel> updateShopImage(
            @Url String url,
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Field("pic_code") String pic_code,// 5
            @Field("pic_src") String pic_src, // 6
            @Field("user_id") String user_id,// 7
            @Field("device_id") String deviceId, // 6
            @Field("hash") String hash,// 7
            @Field("device_time") String deviceTime// 8
    );
}
