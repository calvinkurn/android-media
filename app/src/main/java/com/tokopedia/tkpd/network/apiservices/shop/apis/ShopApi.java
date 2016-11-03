package com.tokopedia.tkpd.network.apiservices.shop.apis;

import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.shopinfo.models.shopnotes.GetShopNotes;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public interface ShopApi {

    @GET(TkpdBaseURL.Shop.PATH_GET_LIKE_DISLIKE_REVIEW)
    Observable<Response<TkpdResponse>> getLikeReview(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_PEOPLE_FAV_MY_SHOP)
    Observable<Response<TkpdResponse>> getWhoFave(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_ETALASE)
    Observable<Response<TkpdResponse>> getEtalase(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_INFO)
    Observable<Response<TkpdResponse>> getInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_LOCATION)
    Observable<Response<TkpdResponse>> getLocation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_NOTES)
    Observable<Response<TkpdResponse>> getNotes(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Shop.PATH_GET_SHOP_PRODUCT)
    Observable<Response<TkpdResponse>> getProduct(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_REVIEW)
    Observable<Response<TkpdResponse>> getReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GEt_SHOP_TALK)
    Observable<Response<TkpdResponse>> getTalk(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.URL_SHOP + TkpdBaseURL.Shop.PATH_GET_SHOP_NOTES)
    Observable<GetShopNotes> getNotesBasic(
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Field("user_id") String userId,// 5
            @Field("device_id") String deviceId, // 6
            @Field("hash") String hash,// 7
            @Field("device_time") String deviceTime,// 8
            @Field("shop_domain") String shopDomain// 9
    );

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_NOTES)
    Observable<GetShopNotes> getNotes2(@FieldMap Map<String, String> params);

}
