package com.tokopedia.tkpd.home.favorite.interactor;

import com.tokopedia.core.network.entity.home.FavoriteSendData;
import com.tokopedia.core.network.entity.home.TopAdsData;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.ShopItem;
import com.tokopedia.tkpd.home.favorite.model.params.WishlistFromNetworkParams;
import com.tokopedia.tkpd.home.model.FavoriteTransformData;

import rx.Subscriber;

/**
 * @author Kulomady on 11/11/16.
 */

public interface FavoriteInteractor {

    void sendListDataFavorite(final ShopItem shopItem, final Object... datas);

    void fetchListDataFavorite(Subscriber<TopAdsData> topAdsData,
                               TKPDMapParam<String, String> favoriteMapParams);

    void fetchWishlistFromNetwork(Subscriber<FavoriteTransformData> subscriber,
                                  WishlistFromNetworkParams params);

    void fetchWishlistFromCache();

    void removeAllSubscriptions();

    void sendData(Subscriber<FavoriteSendData> favoriteSendDataSubscriber,
                  TKPDMapParam<String, String> stringStringTKPDMapParam);

    void fetchFavoriteShop(Subscriber<TopAdsData> topAdsDataSubscriber,
                           TKPDMapParam<String, String> params);
}
