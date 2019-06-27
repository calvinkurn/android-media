package com.tokopedia.core.shopinfo.facades.authservices;


import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Tkpd_Eka on 12/3/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */

@Deprecated
public interface ShopApi {
    @FormUrlEncoded
    @POST("get_shop_info.pl")
    Observable<Response<TkpdResponse>> getShopInfo(@FieldMap Map<String, String> params);

    @GET("get_shop_etalase.pl")
    Observable<Response<TkpdResponse>> getShopEtalase(@QueryMap Map<String, String> params);

    @GET("v1/web-service/shop/get_shop_product")
    Observable<Response<TkpdResponse>> getShopProduct(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_shop_talk.pl")
    Observable<Response<TkpdResponse>> getShopTalk(@FieldMap Map<String, String> params);

    @GET("get_shop_review.pl")
    Observable<Response<TkpdResponse>> getShopReview(@QueryMap Map<String, String> params);

    @GET("get_like_dislike_review_shop.pl")
    Observable<Response<TkpdResponse>> getLikeDislike(@QueryMap Map<String, String> params);

    @GET("get_people_who_favorite_myshop.pl")
    Observable<Response<TkpdResponse>> getPeopleFavorite(@QueryMap TKPDMapParam<String, String> params);


}
