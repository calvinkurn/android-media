package com.tokopedia.tkpd.home.favorite.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import rx.Observable;

/**
 * @author Kulomady on 1/18/17.
 */

public class FavoriteDataRepository implements FavoriteRepository {
    private final FavoriteFactory mFavoriteFactory;

    public FavoriteDataRepository(FavoriteFactory favoriteFactory) {
        mFavoriteFactory = favoriteFactory;
    }

    @Override
    public Observable<DomainWishlist> getWishlist(TKPDMapParam<String, Object> param) {
        return mFavoriteFactory.getWishlist(param);
    }

    @Override
    public Observable<FavoriteShop> getFavoriteShop(TKPDMapParam<String, String> param) {
        return mFavoriteFactory.getFavoriteShop(param);
    }

    @Override
    public Observable<TopAdsShop> getTopAdsShop(TKPDMapParam<String, Object> params) {
        return mFavoriteFactory.getTopAdsShop(params);
    }
}
