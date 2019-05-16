package com.tokopedia.search.result.data.mapper.favoriteshoplist;

import com.tokopedia.search.result.domain.model.FavoriteShopListModel;

import retrofit2.Response;
import rx.functions.Func1;

final class FavoriteShopListMapper implements Func1<Response<FavoriteShopListModel>, FavoriteShopListModel> {

    @Override
    public FavoriteShopListModel call(Response<FavoriteShopListModel> favoriteShopListModelResponse) {
        return null;
    }
}
