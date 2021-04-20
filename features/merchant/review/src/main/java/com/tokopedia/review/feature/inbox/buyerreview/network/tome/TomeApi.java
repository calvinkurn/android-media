package com.tokopedia.review.feature.inbox.buyerreview.network.tome;


import com.tokopedia.review.feature.inbox.buyerreview.data.pojo.FavoriteCheckResult;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.tokopedia.review.feature.inbox.buyerreview.network.ReputationBaseURL.PATH_IS_FAVORITE_SHOP;

public interface TomeApi {

    String USER_ID = "user_id";
    String SHOP_ID = "shop_id";

    @GET(PATH_IS_FAVORITE_SHOP)
    Observable<Response<FavoriteCheckResult>> checkIsShopFavorited(
            @Query(USER_ID) String userId,
            @Query(SHOP_ID) String shopIds
    );
}
