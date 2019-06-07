package com.tokopedia.search.result.network.service;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.search.result.domain.model.FavoriteShopListModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface TomeApi {

    @GET(SearchConstant.Tome.PATH_IS_FAVORITE_SHOP)
    Observable<Response<FavoriteShopListModel>> checkIsShopFavorited(@QueryMap Map<String, Object> queryParams);
}
