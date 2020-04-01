package com.tokopedia.favorite.data;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.favorite.domain.FavoriteRepository;
import com.tokopedia.favorite.domain.model.FavShop;
import com.tokopedia.favorite.domain.model.FavoriteShop;
import com.tokopedia.favorite.domain.model.TopAdsShop;

import java.util.HashMap;

import rx.Observable;

/**
 * @author Kulomady on 1/18/17.
 */

public class FavoriteDataRepository implements FavoriteRepository {
    private final FavoriteFactory favoriteFactory;

    public FavoriteDataRepository(FavoriteFactory favoriteFactory) {
        this.favoriteFactory = favoriteFactory;
    }

    @Override
    public Observable<FavoriteShop> getFirstPageFavoriteShop(HashMap<String, String> params) {
        return favoriteFactory.getFavoriteShopFirstPage(params);
    }

    @Override
    public Observable<FavoriteShop> getFavoriteShop(
            HashMap<String, String> param) {

        return favoriteFactory.getFavoriteShop(param);
    }

    @Override
    public Observable<TopAdsShop> getFreshTopAdsShop(HashMap<String, Object> params) {
        return favoriteFactory.getFreshTopAdsShop(params);
    }

    @Override
    public Observable<TopAdsShop> getTopAdsShop(HashMap<String, Object> params) {
        return favoriteFactory.getTopAdsShop(params);
    }
}
