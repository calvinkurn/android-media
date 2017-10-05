package com.tokopedia.tkpd.home.favorite.view.viewmodel;

import java.util.List;

/**
 * @author Kulomady on 1/27/17.
 */

public class WishlistItem {
    private String productId;
    private String name;
    private String price;
    private String shopName;
    private String productImage;
    private List<String> badgeImageUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public List<String> getBadgeImageUrl() {
        return badgeImageUrl;
    }

    public void setBadgeImageUrl(List<String> badgeImageUrl) {
        this.badgeImageUrl = badgeImageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
