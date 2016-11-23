package com.tokopedia.home.interactor;

import com.tokopedia.core.network.entity.home.ProductFeedData;
import com.tokopedia.core.network.entity.home.WishlistData;
import com.tokopedia.core.var.ShopItem;
import com.tokopedia.home.model.FavoriteTransformData;
import com.tokopedia.home.model.ProductFeedTransformData;

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

    Observable<com.tokopedia.home.model.HorizontalShopList> getShopAdsObservable();
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
