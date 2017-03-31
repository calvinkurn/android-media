package com.tokopedia.tkpd.home.favorite.domain.interactor;

/**
 * @author kulomady on 3/20/17.
 */

public class ShopItemParam {
    private String shopId;
    private String shopName;
    private String shopCoverUrl;
    private String shopImageUrl;
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

    public static void setAdKey(ShopItemParam shopItemParam, String adKey) {
        shopItemParam.adKey = adKey;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }
}