package com.tokopedia.home.account.favorite.data.source.apis.service;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */

public interface ServiceV4 {

    @FormUrlEncoded
    @POST("get_wishlist.pl")
    Observable<Response<String>> getWishlist(@FieldMap HashMap<String, String> param);


    @FormUrlEncoded
    @POST("v4/action/favorite-shop/fav_shop.pl")
    Observable<Response<String>> postFavoriteShop(@FieldMap Map<String, String> params);
}
