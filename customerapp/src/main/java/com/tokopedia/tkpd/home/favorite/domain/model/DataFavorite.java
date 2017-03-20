package com.tokopedia.tkpd.home.favorite.domain.model;

/**
 * @author Kulomady on 1/19/17.
 */
public class DataFavorite {
    private DomainWishlist mWishListData;
    private FavoriteShop mFavoriteShop;
    private TopAdsShop topAdsShop;

    public DomainWishlist getWishListData() {
        return mWishListData;
    }

    public void setWishListData(DomainWishlist wishListData) {
        this.mWishListData = wishListData;
    }

    public void setFavoriteShop(FavoriteShop favoriteShop) {

        mFavoriteShop = favoriteShop;
    }

    public FavoriteShop getFavoriteShop() {
        return mFavoriteShop;
    }

    public TopAdsShop getTopAdsShop() {
        return topAdsShop;
    }

    public void setTopAdsShop(TopAdsShop topAdsShop) {
        this.topAdsShop = topAdsShop;
    }
}
