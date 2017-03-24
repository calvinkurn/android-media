package com.tokopedia.tkpd.home.favorite.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import rx.Observable;

/**
 * @author Kulomady on 1/18/17.
 */

public interface FavoriteRepository {

    Observable<DomainWishlist> getWishlist(TKPDMapParam<String, Object> params);

    Observable<DomainWishlist> getFreshWishlist(TKPDMapParam<String, Object> params);

    Observable<FavoriteShop> getFirstPageFavoriteShop(TKPDMapParam<String,String> params);

    Observable<FavoriteShop> getFavoriteShop(
            TKPDMapParam<String, String> param);

    Observable<TopAdsShop> getFreshTopAdsShop(TKPDMapParam<String, Object> params);

    Observable<TopAdsShop> getTopAdsShop(TKPDMapParam<String, Object> params);

    Observable<FavShop> addFavoriteShop(TKPDMapParam<String, String> param);
}
