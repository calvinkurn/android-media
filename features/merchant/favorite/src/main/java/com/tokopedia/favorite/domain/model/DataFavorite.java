package com.tokopedia.favorite.domain.model;

/**
 * @author Kulomady on 1/19/17.
 */
public class DataFavorite {
    private FavoriteShop favoriteShop;
    private TopAdsShop topAdsShop;

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
