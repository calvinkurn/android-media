package com.tokopedia.tkpd.home.favorite.domain.model;

import java.util.List;

/**
 * @author Kulomady on 1/23/17.
 */

public class DataWishlist {
    private String id;
    private String name;
    private String price;
    private String productImageUrl;

    private List<WishListLabel> labels;
    private List<WishListBadge> badges;
    private String shop_location;
    private String shop_name;
    private boolean isAvailable;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<WishListLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<WishListLabel> labels) {
        this.labels = labels;
    }

    public List<WishListBadge> getBadges() {
        return badges;
    }

    public void setBadges(List<WishListBadge> badges) {
        this.badges = badges;
    }

    public String getShop_location() {
        return shop_location;
    }

    public void setShop_location(String shop_location) {
        this.shop_location = shop_location;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
}
