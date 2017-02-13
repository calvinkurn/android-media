package com.tokopedia.tkpd.home.feed.domain.model;

/**
 * @author Kulomady on 12/8/16.
 */

public class TopAds {
    private String id;
    private String adRefKey;
    private String redirect;
    private String stickerId;
    private String stickerImage;
    private String productClickUrl;
    private String shopClickUrl;
    private TopAdsProduct product;
    private TopAdsShop shop;
    private boolean isValid;

    public boolean isValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStickerId() {
        return stickerId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }

    public String getStickerImage() {
        return stickerImage;
    }

    public void setStickerImage(String stickerImage) {
        this.stickerImage = stickerImage;
    }

    public String getProductClickUrl() {
        return productClickUrl;
    }

    public void setProductClickUrl(String productClickUrl) {
        this.productClickUrl = productClickUrl;
    }

    public String getShopClickUrl() {
        return shopClickUrl;
    }

    public void setShopClickUrl(String shopClickUrl) {
        this.shopClickUrl = shopClickUrl;
    }

    public TopAdsProduct getProduct() {
        return product;
    }

    public void setProduct(TopAdsProduct product) {
        this.product = product;
    }

    public TopAdsShop getShop() {
        return shop;
    }

    public void setShop(TopAdsShop shop) {
        this.shop = shop;
    }
}
