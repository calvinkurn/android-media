package com.tokopedia.home.beranda.presentation.view.viewmodel;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.domain.gql.feed.Badge;
import com.tokopedia.home.beranda.domain.gql.feed.Label;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory;
import com.tokopedia.kotlin.model.ImpressHolder;

import java.util.List;

public class HomeFeedViewModel extends ImpressHolder implements Visitable<HomeFeedTypeFactory> {
    private static final String DATA_NONE_OTHER = "none / other";
    private static final String DATA_NAME = "name";
    private static final String DATA_ID = "id";
    private static final String DATA_PRICE = "price";
    private static final String DATA_BRAND = "brand";
    private static final String DATA_VARIANT = "variant";
    private static final String DATA_CATEGORY = "category";
    private static final String DATA_LIST = "list";
    private static final String DATA_POSITION = "position";
    private static final String DATA_LIST_VALUE = "/ - p2 - %s - rekomendasi untuk anda - %s";
    private static final String DATA_LIST_VALUE_NON_LOGIN = "/ - p2 - non login - %s - rekomendasi untuk anda - %s";

    private final int position;
    private String productId;
    private String productName;
    private String categoryBreadcrumbs;
    private String recommendationType;
    private String imageUrl;
    private String price;
    private String slashedPrice;
    private int discountPercentage;
    private final int rating;
    private final int countReview;
    private String clickUrl;
    private String trackerImageUrl;
    private int priceNumber;
    private boolean isTopAds;
    private List<Label> labels;
    private List<Badge> badges;
    private String location;
    private String wishlistUrl;
    private boolean isWishList;

    public HomeFeedViewModel(String productId,
                             String productName,
                             String categoryBreadcrumbs,
                             String recommendationType,
                             String imageUrl,
                             String price,
                             int rating,
                             int countReview,
                             String clickUrl,
                             String trackerImageUrl,
                             String slashedPrice,
                             int discountPercentage,
                             int priceNumber,
                             boolean isTopAds,
                             int position,
                             List<Label> labels,
                             List<Badge> badges,
                             String location,
                             String wishlistUrl,
                             boolean isWishList) {
        this.productId = productId;
        this.productName = productName;
        this.categoryBreadcrumbs = categoryBreadcrumbs;
        this.recommendationType = recommendationType;
        this.imageUrl = imageUrl;
        this.price = price;
        this.rating = rating;
        this.countReview = countReview;
        this.clickUrl = clickUrl;
        this.trackerImageUrl = trackerImageUrl;
        this.slashedPrice = slashedPrice;
        this.discountPercentage = discountPercentage;
        this.priceNumber = priceNumber;
        this.isTopAds = isTopAds;
        this.position = position;
        this.labels = labels;
        this.badges = badges;
        this.location = location;
        this.wishlistUrl = wishlistUrl;
        this.isWishList = isWishList;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public int getPosition() {
        return position;
    }

    public String getCategoryBreadcrumbs() {
        return categoryBreadcrumbs;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public String getTrackerImageUrl() {
        return trackerImageUrl;
    }

    public void setCategoryBreadcrumbs(String categoryBreadcrumbs) {
        this.categoryBreadcrumbs = categoryBreadcrumbs;
    }

    public String getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }

    public int getPriceNumber() {
        return priceNumber;
    }

    public void setPriceNumber(int priceNumber) {
        this.priceNumber = priceNumber;
    }

    public boolean isTopAds() {
        return isTopAds;
    }

    public String getSlashedPrice() {
        return slashedPrice;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public int getRating() {
        return rating;
    }

    public int getCountReview() {
        return countReview;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public String getLocation() {
        return location;
    }

    public String getWishlistUrl(){ return wishlistUrl; }

    public boolean isWishList() { return isWishList; }

    public void setWishList(boolean value){
        isWishList = value;
    }

    @Override
    public int type(HomeFeedTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public Object convertFeedTabModelToImpressionDataForLoggedInUser(
            String tabName
    ) {
        return DataLayer.mapOf(
                DATA_NAME, getProductName(),
                DATA_ID, getProductId(),
                DATA_PRICE, getPriceNumber(),
                DATA_BRAND, DATA_NONE_OTHER,
                DATA_VARIANT, DATA_NONE_OTHER,
                DATA_CATEGORY, getCategoryBreadcrumbs(),
                DATA_LIST, String.format(
                        DATA_LIST_VALUE,
                        tabName,
                        getRecommendationType()
                ),
                DATA_POSITION, String.valueOf(getPosition()));
    }

    public Object convertFeedTabModelToImpressionDataForNonLoginUser(
            String tabName
    ) {
        return DataLayer.mapOf(
                DATA_NAME, getProductName(),
                DATA_ID, getProductId(),
                DATA_PRICE, getPriceNumber(),
                DATA_BRAND, DATA_NONE_OTHER,
                DATA_VARIANT, DATA_NONE_OTHER,
                DATA_CATEGORY, getCategoryBreadcrumbs(),
                DATA_LIST, String.format(
                        DATA_LIST_VALUE_NON_LOGIN,
                        tabName,
                        getRecommendationType()
                ),
                DATA_POSITION, String.valueOf(getPosition()));
    }

    public Object convertFeedTabModelToClickData() {
        return DataLayer.mapOf(
                DATA_NAME, getProductName(),
                DATA_ID, getProductId(),
                DATA_PRICE, getPriceNumber(),
                DATA_BRAND, DATA_NONE_OTHER,
                DATA_VARIANT, DATA_NONE_OTHER,
                DATA_CATEGORY, getCategoryBreadcrumbs(),
                DATA_POSITION, String.valueOf(getPosition()));
    }
}
