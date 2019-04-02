package com.tokopedia.topads.sdk.domain;

import com.tokopedia.topads.sdk.domain.model.WishlistModel;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Author errysuprayogi on 04,December,2018
 */
public interface TopAdsWishlistService {

    @GET
    Observable<Response<WishlistModel>> wishlistUrl(@Url String wishlistUrl);

}
