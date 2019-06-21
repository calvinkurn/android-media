package com.tokopedia.search.result.data.mapper.favoriteshoplist;

import com.tokopedia.search.result.domain.model.FavoriteShopListModel;

import retrofit2.Response;
import rx.functions.Func1;

final class FavoriteShopListErrorMapper implements Func1<Throwable, Response<FavoriteShopListModel>> {

    @Override
    public Response<FavoriteShopListModel> call(Throwable throwable) {
        return Response.success(new FavoriteShopListModel());
    }
}
