package com.tokopedia.tkpd.home.favorite.view.viewmodel;

/**
 * @author by erry on 30/01/17.
 */

public class TopAdsShopItem {


    private String shopId;
    private String shopName;
    private String shopCoverUrl;
    private String shopCoverEcs;
    private String shopImageUrl;
    private String shopImageEcs;
    private String shopLocation;
    private boolean isFav;
    private String shopClickUrl;
    private String adKey;
    private String shopDomain;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopCoverUrl() {
        return shopCoverUrl;
    }

    public void setShopCoverUrl(String shopCoverUrl) {
        this.shopCoverUrl = shopCoverUrl;
    }

    public String getShopCoverEcs() {
        return shopCoverEcs;
    }

    public void setShopCoverEcs(String shopCoverEcs) {
        this.shopCoverEcs = shopCoverEcs;
    }

    public String getShopImageUrl() {
        return shopImageUrl;
    }

    public void setShopImageUrl(String shopImageUrl) {
        this.shopImageUrl = shopImageUrl;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getShopClickUrl() {
        return shopClickUrl;
    }

    public void setShopClickUrl(String shopClickUrl) {
        this.shopClickUrl = shopClickUrl;
    }

    public String getAdKey() {
        return adKey;
    }

    public void setAdKey(String adKey) {
        this.adKey = adKey;
    }

    public void setShopImageEcs(String shopImageEcs) {
        this.shopImageEcs = shopImageEcs;
    }

    public String getShopImageEcs() {
        return shopImageEcs;
    }
}
