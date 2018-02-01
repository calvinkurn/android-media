package com.tokopedia.tkpd.beranda.data.source.api;

import com.tokopedia.tkpd.beranda.data.source.pojo.HomeData;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.GraphqlResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by henrypriyono on 26/01/18.
 */

public interface HomeDataApi {
    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<HomeData>>> getHomeData(@Body String requestBody);
}