package com.tokopedia.tkpd.home.favorite.domain.model;

/**
 * @author kulomady on 1/24/17.
 */
public class TopAdsShopItem {

    private String id;
    private String adRefKey;
    private String redirect;
    private String shopClickUrl;

    private String shopId;
    private String shopLocation;
    private String goldShop;
    private String shopName;
    private String luckyShop;
    private String shopUri;
    private String shopImageCover;
    private String shopImageCoverEcs;
    private String shopImageUrl;
    private String shopImageEcs;
    private String shopDomain;

    private boolean isSelected;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public String getAdRefKey() {
        return adRefKey;
    }

    public void setAdRefKey(String adRefKey) {
        this.adRefKey = adRefKey;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getShopClickUrl() {
        return shopClickUrl;
    }

    public void setShopClickUrl(String shopClickUrl) {
        this.shopClickUrl = shopClickUrl;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getGoldShop() {
        return goldShop;
    }

    public void setGoldShop(String goldShop) {
        this.goldShop = goldShop;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLuckyShop() {
        return luckyShop;
    }

    public void setLuckyShop(String luckyShop) {
        this.luckyShop = luckyShop;
    }

    public String getShopUri() {
        return shopUri;
    }

    public void setShopUri(String shopUri) {
        this.shopUri = shopUri;
    }

    public String getShopImageCover() {
        return shopImageCover;
    }

    public void setShopImageCover(String shopImageCover) {
        this.shopImageCover = shopImageCover;
    }

    public String getShopImageCoverEcs() {
        return shopImageCoverEcs;
    }

    public void setShopImageCoverEcs(String shopImageCoverEcs) {
        this.shopImageCoverEcs = shopImageCoverEcs;
    }

    public String getShopImageUrl() {
        return shopImageUrl;
    }

    public void setShopImageUrl(String shopImageUrl) {
        this.shopImageUrl = shopImageUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setShopImageEcs(String sEcs) {
        this.shopImageEcs = sEcs;
    }

    public String getShopImageEcs() {
        return shopImageEcs;
    }
}
