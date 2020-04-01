package com.tokopedia.favorite.domain;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.favorite.domain.model.FavShop;
import com.tokopedia.favorite.domain.model.FavoriteShop;
import com.tokopedia.favorite.domain.model.TopAdsShop;

import java.util.HashMap;

import rx.Observable;

/**
 * @author Kulomady on 1/18/17.
 */

public interface FavoriteRepository {

    Observable<FavoriteShop> getFirstPageFavoriteShop(HashMap<String, String> params);

    Observable<FavoriteShop> getFavoriteShop(
            HashMap<String, String> param);

    Observable<TopAdsShop> getFreshTopAdsShop(HashMap<String, Object> params);

    Observable<TopAdsShop> getTopAdsShop(HashMap<String, Object> params);
}
