package com.tokopedia.core.home.favorite.model.params;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author Kulomady on 11/11/16.
 */

public class WishlistFromNetworkParams {

    private TKPDMapParam<String, String> wishlistParams;
    private TopAddParams topAddParams;
    private TKPDMapParam<String, String> favoriteMapParams;
    private Boolean refreshTopAds;

    public TKPDMapParam<String, String> getWishlistParams() {
        return wishlistParams;
    }

    public void setWishlistParams(TKPDMapParam<String, String> wishlistParams) {
        this.wishlistParams = wishlistParams;
    }

    public TopAddParams getTopAdsParams() {
        return topAddParams;
    }

    public void setTopAdsParams(TopAddParams topAddParams) {
        this.topAddParams = topAddParams;
    }

    public TKPDMapParam<String, String> getFavoriteMapParams() {
        return favoriteMapParams;
    }

    public void setFavoriteMapParams(TKPDMapParam<String, String> favoriteMapParams) {
        this.favoriteMapParams = favoriteMapParams;
    }

    public Boolean getRefreshTopAds() {
        return refreshTopAds;
    }

    public void setRefreshTopAds(Boolean refreshTopAds) {
        this.refreshTopAds = refreshTopAds;
    }
}
