package com.tokopedia.recentview.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductItem {

    @SerializedName("product_id")
    @Expose
    public String id;// 1

    @SerializedName("product_name")
    @Expose
    public String name;// 2

    @SerializedName("product_price")
    @Expose
    public String price;// 3

    @SerializedName("shop_gold_status")
    @Expose
    public int isNewGold;// 4

    @SerializedName("shop_name")
    @Expose
    public String shop;// 5

    @SerializedName("product_image")
    @Expose
    public String imgUri;// 6

    public String isGold;// this is replace by isNewGold

    @SerializedName("shop_lucky")
    @Expose
    public String luckyShop;// 7

    @SerializedName("shop_id")
    @Expose
    int shopId;// 8


    @SerializedName("product_preorder")
    @Expose
    public String preorder;


    @SerializedName("product_wholesale")
    public String wholesale;

    @SerializedName("labels")
    @Expose
    public List<Label> labels = new ArrayList<Label>();

    @SerializedName("badges")
    @Expose
    public List<Badge> badges = new ArrayList<Badge>();

    @SerializedName("shop_location")
    public String shop_location;

    @SerializedName("free_return")
    public String free_return;

    @SerializedName("rating")
    public String rating;

    @SerializedName("review_count")
    public String reviewCount;

    @SerializedName("official_store")
    public boolean isOfficial = false;

    Boolean isWishlist = false;

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

    public int getIsNewGold() {
        return isNewGold;
    }

    public void setIsNewGold(int isNewGold) {
        this.isNewGold = isNewGold;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getIsGold() {
        return isGold;
    }

    public void setIsGold(String isGold) {
        this.isGold = isGold;
    }

    public String getLuckyShop() {
        return luckyShop;
    }

    public void setLuckyShop(String luckyShop) {
        this.luckyShop = luckyShop;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getPreorder() {
        return preorder;
    }

    public void setPreorder(String preorder) {
        this.preorder = preorder;
    }

    public String getWholesale() {
        return wholesale;
    }

    public void setWholesale(String wholesale) {
        this.wholesale = wholesale;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public String getShop_location() {
        return shop_location;
    }

    public void setShop_location(String shop_location) {
        this.shop_location = shop_location;
    }

    public String getFree_return() {
        return free_return;
    }

    public void setFree_return(String free_return) {
        this.free_return = free_return;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public Boolean getWishlist() {
        return isWishlist;
    }

    public void setWishlist(Boolean wishlist) {
        isWishlist = wishlist;
    }
}
