package com.tokopedia.tkpd.home.interactor;

import com.tokopedia.tkpd.home.model.HorizontalShopList;
import com.tokopedia.tkpd.home.model.network.FavoriteTransformData;
import com.tokopedia.tkpd.home.model.network.ProductFeedData;
import com.tokopedia.tkpd.home.model.network.ProductFeedTransformData;
import com.tokopedia.tkpd.home.model.network.WishlistData;
import com.tokopedia.tkpd.var.ShopItem;

import java.util.List;

import rx.Observable;

/**
 * Created by ricoharisin on 12/7/15.
 */
public interface CacheHomeInteractor {

    void getProductFeedCache(GetProductFeedCacheListener listener);
    void setProductFeedCache(ProductFeedTransformData productFeedTransformData);
    void getFavoriteCache(GetFavoriteCacheListener listener);
    void setFavoriteCache(FavoriteTransformData favoriteTransformData);
    void setProdHistoryCache(ProductFeedData productFeedData);
    ProductFeedData getProdHistoryCache();
    void setWishListCache(WishlistData wishlistData);
    WishlistData getWishListCache();
    Observable<HorizontalShopList> getShopAdsObservable();
    Boolean isShopAdsCacheAvailable();
    void setRecommendedShopCache(List<ShopItem> shopItem);
    void unSubscribeObservable();

    interface GetProductFeedCacheListener {
        void onSuccess(ProductFeedTransformData productFeedTransformData);
        void onError(Throwable e);
    }

    interface GetFavoriteCacheListener {
        void onSuccess(FavoriteTransformData favoriteTransformData);
        void onError(Throwable e);
    }



}
