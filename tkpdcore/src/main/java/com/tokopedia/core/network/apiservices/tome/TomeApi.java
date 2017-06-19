package com.tokopedia.core.network.apiservices.tome;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
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
}
