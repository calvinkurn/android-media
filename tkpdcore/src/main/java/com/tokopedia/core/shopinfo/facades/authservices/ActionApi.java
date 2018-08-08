package com.tokopedia.core.shopinfo.facades.authservices;


import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Tkpd_Eka on 12/7/2015.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface ActionApi {
    String PARAM_SHOP_ID = "shop_id";
    String PARAM_SHOP_DOMAIN = "shop_domain";
    String PARAM_SHOP_SRC = "src";
    String PARAM_SHOP_AD_KEY = "ad_key";

    @FormUrlEncoded
    @POST("favorite-shop/fav_shop.pl")
    Observable<Response<TkpdResponse>> actionFavoriteShop(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("review/like_dislike_review.pl")
    Observable<Response<TkpdResponse>> actionLikeDislikeReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("talk/follow_product_talk.pl")
    Observable<Response<TkpdResponse>> actionFollowTalk(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("talk/delete_product_talk.pl")
    Observable<Response<TkpdResponse>> actionDeleteTalk(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("talk/report_product_talk.pl")
    Observable<Response<TkpdResponse>> actionReportTalk(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("favorite-shop/fav_shop.pl")
    Observable<Response<TkpdResponse>> actionFavoriteShop(@FieldMap TKPDMapParam<String, Object> params);

}
