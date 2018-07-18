package com.tokopedia.feedplus.data.repository;

import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by stevenfredian on 5/26/17.
 */

public interface FavoriteShopRepository {

    Observable<Boolean> doFavoriteShop(RequestParams requestParams);
}
