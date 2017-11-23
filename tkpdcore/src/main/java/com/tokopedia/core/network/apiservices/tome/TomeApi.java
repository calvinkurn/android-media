package com.tokopedia.core.network.apiservices.tome;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

import static com.tokopedia.core.network.constants.TkpdBaseURL.Tome.PATH_GET_SHOP_PRODUCT;
import static com.tokopedia.core.network.constants.TkpdBaseURL.Tome.PATH_IS_FAVORITE_SHOP;

/**
 * Created by HenryPri on 24/05/17.
 */

public interface TomeApi {
    String USER_ID = "user_id";
    String SHOP_ID = "shop_id";
    String PRODUCT_ID = "p_id";

    @GET(PATH_IS_FAVORITE_SHOP)
    Observable<Response<FavoriteCheckResult>> checkIsShopFavorited(
            @Query(USER_ID) String userId,
            @Query(SHOP_ID) String shopIds
    );


    @GET(PATH_GET_SHOP_PRODUCT)
    Observable<Response<TkpdResponse>> getShopProduct(@QueryMap Map<String, String> params);
}
