package com.tokopedia.search.result.network.service;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface TopAdsService {

    @GET
    Observable<Response<Object>> productWishlistUrl(@Url String wishlistUrl);
}
