package com.tokopedia.search.result.data.mapper.favoriteshoplist;

import com.tokopedia.search.result.domain.model.FavoriteShopListModel;

import retrofit2.Response;
import rx.functions.Func1;

final class FavoriteShopListMapper implements Func1<Response<FavoriteShopListModel>, FavoriteShopListModel> {

    @Override
    public FavoriteShopListModel call(Response<FavoriteShopListModel> favoriteShopListModelResponse) {
        if(favoriteShopListModelResponse.isSuccessful()) {
            FavoriteShopListModel favoriteShopListModel = favoriteShopListModelResponse.body();

            if(favoriteShopListModel != null) {
                return favoriteShopListModel;
            }
            else {
                return new FavoriteShopListModel();
            }
        }
        else {
            return new FavoriteShopListModel();
        }
    }
}
