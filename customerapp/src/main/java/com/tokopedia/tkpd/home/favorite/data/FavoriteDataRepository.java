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
    public Observable<DomainWishlist> getWishlist(TKPDMapParam<String, Object> params) {

        return favoriteFactory.getWishlist(params);
    }

    @Override
    public Observable<DomainWishlist> getFreshWishlist(TKPDMapParam<String, Object> params) {
        return favoriteFactory.getFreshWishlist(params);
    }

    @Override
    public Observable<FavoriteShop> getFirstPageFavoriteShop(TKPDMapParam<String, String> params) {
        return favoriteFactory.getFavoriteShopFirstPage(params);
    }

    @Override
    public Observable<FavoriteShop> getFavoriteShop(
            TKPDMapParam<String, String> param) {

        return favoriteFactory.getFavoriteShop(param);
    }

    @Override
    public Observable<TopAdsShop> getFreshTopAdsShop(TKPDMapParam<String, Object> params) {
        return favoriteFactory.getFreshTopAdsShop(params);
    }

    @Override
    public Observable<TopAdsShop> getTopAdsShop(TKPDMapParam<String, Object> params) {
        return favoriteFactory.getTopAdsShop(params);
    }

    @Override
    public Observable<FavShop> addFavoriteShop(TKPDMapParam<String, String> param) {
        return favoriteFactory.postFavShop(param);
    }
}
