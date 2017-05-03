package com.tokopedia.tkpd.home.wishlist.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kulomady on 2/20/17.
 */

@SuppressWarnings("unused")
public class WishlistDomain {

    private String id;
    private String name;
    private String url;
    private String imageUrl;
    private int price;
    private String condition;
    private boolean isAvailable;
    private String status;
    private String priceFormated;
    private int minimumOrder;
    private List<WholesalePriceDomain> wholeSalePrice = new ArrayList<>();
    private ShopWishlistDomain shop;
    private boolean isPreOrder;
    private List<BadgeWishlistDomain> badges;
    private List<LabelWishlistDomain> labels;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriceFormated() {
        return priceFormated;
    }

    public void setPriceFormated(String priceFormated) {
        this.priceFormated = priceFormated;
    }

    public int getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(int minimumOrder) {
        this.minimumOrder = minimumOrder;
    }

    public List<WholesalePriceDomain> getWholeSalePrice() {
        return wholeSalePrice;
    }

    public void setWholeSalePrice(List<WholesalePriceDomain> wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
    }

    public ShopWishlistDomain getShop() {
        return shop;
    }

    public void setShop(ShopWishlistDomain shop) {
        this.shop = shop;
    }

    public boolean isPreOrder() {
        return isPreOrder;
    }

    public void setPreOrder(boolean preOrder) {
        isPreOrder = preOrder;
    }

    public List<BadgeWishlistDomain> getBadges() {
        return badges;
    }

    public void setBadges(List<BadgeWishlistDomain> badges) {
        this.badges = badges;
    }

    public List<LabelWishlistDomain> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelWishlistDomain> labels) {
        this.labels = labels;
    }
}
