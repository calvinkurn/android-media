package com.tokopedia.search.result.data.source.favoriteshoplist;

import com.tokopedia.search.result.domain.model.FavoriteShopListModel;
import com.tokopedia.search.result.network.service.TomeApi;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

public class FavoriteShopListDataSource {

    private TomeApi tomeApi;
    private Func1<Response<FavoriteShopListModel>, FavoriteShopListModel> favoriteShopListMapper;
    private Func1<Throwable, Response<FavoriteShopListModel>> favoriteShopListErrorMapper;

    FavoriteShopListDataSource(TomeApi tomeApi,
                               Func1<Response<FavoriteShopListModel>, FavoriteShopListModel> favoriteShopListMapper,
                               Func1<Throwable, Response<FavoriteShopListModel>> favoriteShopListErrorMapper) {
        this.tomeApi = tomeApi;
        this.favoriteShopListMapper = favoriteShopListMapper;
        this.favoriteShopListErrorMapper = favoriteShopListErrorMapper;
    }

    public Observable<FavoriteShopListModel> getFavoriteShopList(Map<String, Object> queryParams) {
        return tomeApi
                .checkIsShopFavorited(queryParams)
                .onErrorReturn(favoriteShopListErrorMapper)
                .map(favoriteShopListMapper);
    }
}
