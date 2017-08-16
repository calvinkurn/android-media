package com.tokopedia.core.resolutioncenter.createreso.service;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by yoasfs on 16/08/17.
 */

public interface CreateResoApi {
    @GET
    Observable<Response<TkpdResponse>> getProductProblem(@Url String url, @QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_shop_talk.pl")
    Observable<Response<TkpdResponse>> getShopTalk(@FieldMap Map<String, String> params);

    @GET("get_shop_review.pl")
    Observable<Response<TkpdResponse>> getShopReview(@QueryMap Map<String, String> params);

}
