package com.tokopedia.search.result.data.repository.favoriteshoplist;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.search.result.data.source.favoriteshoplist.FavoriteShopListDataSource;
import com.tokopedia.search.result.domain.model.FavoriteShopListModel;

import java.util.Map;

import rx.Observable;

final class FavoriteShopListRepository implements Repository<FavoriteShopListModel> {

    private FavoriteShopListDataSource favoriteShopListDataSource;

    FavoriteShopListRepository(FavoriteShopListDataSource favoriteShopListDataSource) {
        this.favoriteShopListDataSource = favoriteShopListDataSource;
    }

    @Override
    public Observable<FavoriteShopListModel> query(Map<String, Object> parameters) {
        return favoriteShopListDataSource.getFavoriteShopList(parameters);
    }
}
