package com.tokopedia.tkpd.tkpdreputation.network.shop;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.tkpd.tkpdreputation.constant.ReputationBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface ShopApi {

    @GET(ReputationBaseURL.PATH_GET_LIKE_DISLIKE_REVIEW)
    Observable<Response<TokopediaWsV4Response>> getLikeReview(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_GET_PEOPLE_FAV_MY_SHOP)
    Observable<Response<TokopediaWsV4Response>> getWhoFave(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_GET_SHOP_ETALASE)
    Observable<Response<TokopediaWsV4Response>> getEtalase(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_GET_SHOP_INFO)
    Observable<Response<TokopediaWsV4Response>> getInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_GET_SHOP_LOCATION)
    Observable<Response<TokopediaWsV4Response>> getLocation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_GET_SHOP_NOTES)
    Observable<Response<TokopediaWsV4Response>> getNotes(@FieldMap Map<String, String> params);

    @GET(ReputationBaseURL.PATH_GET_SHOP_PRODUCT)
    Observable<Response<TokopediaWsV4Response>> getProduct(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ReputationBaseURL.PATH_GET_SHOP_REVIEW)
    Observable<Response<TokopediaWsV4Response>> getReview(@FieldMap Map<String, String> params);

}
