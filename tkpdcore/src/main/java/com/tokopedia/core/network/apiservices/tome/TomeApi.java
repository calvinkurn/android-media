package com.tokopedia.core.network.apiservices.tome;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by HenryPri on 24/05/17.
 */

public interface TomeApi {
    String PATH_IS_FAVORITE_SHOP = "v1/user/isfollowing";
    String USER_ID = "user_id";
    String SHOP_ID = "shop_id";

    @GET(PATH_IS_FAVORITE_SHOP)
    Observable<Response<FavoriteCheckResult>> checkIsShopFavorited(
            @Query(USER_ID) String userId,
            @Query(SHOP_ID) String shopIds
    );


    @GET("v1/web-service/shop/get_shop_product")
    Observable<Response<TkpdResponse>> getShopProduct(@QueryMap Map<String, String> params);
}
