package com.tokopedia.tkpd.home.favorite.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

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
    public Observable<DomainWishlist> getWishlist(
            TKPDMapParam<String, Object> params, boolean isForceRefresh) {

        return favoriteFactory.getWishlist(params, isForceRefresh);
    }

    @Override
    public Observable<FavoriteShop> getFavoriteShop(
            TKPDMapParam<String, String> param, boolean isFirstPage) {

        return favoriteFactory.getFavoriteShop(param, isFirstPage);
    }

    @Override
    public Observable<TopAdsShop> getTopAdsShop(
            TKPDMapParam<String, Object> params, boolean isFreshData) {
        return favoriteFactory.getTopAdsShop(params, isFreshData);
    }

    @Override
    public Observable<FavShop> addFavoriteShop(TKPDMapParam<String, String> param) {
        return favoriteFactory.postFavShop(param);
    }
}
