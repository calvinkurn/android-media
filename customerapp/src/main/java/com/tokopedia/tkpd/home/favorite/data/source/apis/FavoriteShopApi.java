package com.tokopedia.tkpd.home.favorite.data.source.apis;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.network.entity.home.FavoritShopResponseData;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;


import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by naveengoyal on 5/7/18.
 */

public interface FavoriteShopApi {

    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<FavoritShopResponseData>>> getFavoritShopsData(@Body String requestBody);
}
