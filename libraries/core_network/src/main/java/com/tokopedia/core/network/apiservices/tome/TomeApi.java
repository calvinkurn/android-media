package com.tokopedia.core.network.apiservices.tome;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.tokopedia.core.network.constants.TkpdBaseURL.Tome.PATH_IS_FAVORITE_SHOP;


/**
 * Created by HenryPri on 24/05/17.
 */

@Deprecated
public interface TomeApi {
    String USER_ID = "user_id";
    String SHOP_ID = "shop_id";
    String PRODUCT_ID = "productId";

    @GET(PATH_IS_FAVORITE_SHOP)
    Observable<Response<FavoriteCheckResult>> checkIsShopFavorited(
            @Query(USER_ID) String userId,
            @Query(SHOP_ID) String shopIds
    );
}
