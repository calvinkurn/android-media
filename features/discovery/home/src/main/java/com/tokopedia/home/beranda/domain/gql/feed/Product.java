
package com.tokopedia.home.beranda.domain.gql.feed;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("category_breadcrumbs")
    @Expose
    private String categoryBreadcrumbs = "";
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("click_url")
    @Expose
    private String clickUrl = "";
    @SerializedName("image_url")
    @Expose
    private String imageUrl = "";
    @SerializedName("is_wishlist")
    @Expose
    private Boolean isWishlist = false;
    @SerializedName("wishlist_url")
    @Expose
    private String wishlistUrl = "";
    @SerializedName("applink")
    @Expose
    private String applink;
    @SerializedName("is_topads")
    @Expose
    private Boolean isTopads;
    @SerializedName("tracker_image_url")
    @Expose
    private String trackerImageUrl = "";
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("price_int")
    @Expose
    private Integer priceInt = 0;
    @SerializedName("slashed_price")
    @Expose
    private String slashedPrice = "";
    @SerializedName("slashed_price_int")
    @Expose
    private Integer slashedPriceInt = 0;
    @SerializedName("rating")
    @Expose
    private Integer rating = 0;
    @SerializedName("count_review")
    @Expose
    private Integer countReview = 0;
    @SerializedName("recommendation_type")
    @Expose
    private String recommendationType = "";

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

    public String getCategoryBreadcrumbs() {
        return categoryBreadcrumbs;
    }

    public void setCategoryBreadcrumbs(String categoryBreadcrumbs) {
        this.categoryBreadcrumbs = categoryBreadcrumbs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsWishlist() {
        return isWishlist;
    }

    public void setIsWishlist(Boolean isWishlist) {
        this.isWishlist = isWishlist;
    }

    public String getWishlistUrl() {
        return wishlistUrl;
    }

    public void setWishlistUrl(String wishlistUrl) {
        this.wishlistUrl = wishlistUrl;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public Boolean getIsTopads() {
        return isTopads;
    }

    public void setIsTopads(Boolean isTopads) {
        this.isTopads = isTopads;
    }

    public String getTrackerImageUrl() {
        return trackerImageUrl;
    }

    public void setTrackerImageUrl(String trackerImageUrl) {
        this.trackerImageUrl = trackerImageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getPriceInt() {
        return priceInt;
    }

    public void setPriceInt(Integer priceInt) {
        this.priceInt = priceInt;
    }

    public String getSlashedPrice() {
        return slashedPrice;
    }

    public void setSlashedPrice(String slashedPrice) {
        this.slashedPrice = slashedPrice;
    }

    public Integer getSlashedPriceInt() {
        return slashedPriceInt;
    }

    public void setSlashedPriceInt(Integer slashedPriceInt) {
        this.slashedPriceInt = slashedPriceInt;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getCountReview() {
        return countReview;
    }

    public void setCountReview(Integer countReview) {
        this.countReview = countReview;
    }

    public String getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }
}
