package com.tokopedia.tkpd.home.favorite.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import rx.Observable;

/**
 * @author Kulomady on 1/18/17.
 */

public interface FavoriteRepository {

    Observable<DomainWishlist> getWishlist(TKPDMapParam<String, Object> param);

    Observable<FavoriteShop> getFavoriteShop(TKPDMapParam<String, String> param);

    Observable<TopAdsShop> getTopAdsShop(TKPDMapParam<String, Object> params);
}
