package com.tokopedia.tkpd.home.favorite.domain.model;

/**
 * @author Kulomady on 1/19/17.
 */
public class DataFavorite {
    private DomainWishlist domainWishlist;
    private FavoriteShop favoriteShop;
    private TopAdsShop topAdsShop;

    public DomainWishlist getWishListData() {
        return domainWishlist;
    }

    public void setWishListData(DomainWishlist wishListData) {
        this.domainWishlist = wishListData;
    }

    public void setFavoriteShop(FavoriteShop favoriteShop) {

        this.favoriteShop = favoriteShop;
    }

    public FavoriteShop getFavoriteShop() {
        return favoriteShop;
    }

    public TopAdsShop getTopAdsShop() {
        return topAdsShop;
    }

    public void setTopAdsShop(TopAdsShop topAdsShop) {
        this.topAdsShop = topAdsShop;
    }
}
