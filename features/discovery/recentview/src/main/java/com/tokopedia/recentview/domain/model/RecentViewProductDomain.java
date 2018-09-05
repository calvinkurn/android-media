package com.tokopedia.recentview.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 7/3/17.
 */

public class RecentViewProductDomain {
    private RecentViewShopDomain shop;
    private String id;
    private String name;
    private String price;
    private String imgUri;
    private String isGold;
    private List<RecentViewLabelDomain> labels = new ArrayList<RecentViewLabelDomain>();
    private String rating;
    private String reviewCount;
    private String freeReturn;
    private List<RecentViewBadgeDomain> badges = new ArrayList<>();
    private String wholesale;
    private String preorder;
    private Boolean isWishlist;

    public RecentViewProductDomain(RecentViewShopDomain shop, String id, String name, String price,
                                   String imgUri, String isGold, List<RecentViewLabelDomain> labels,
                                   String rating, String reviewCount, String freeReturn,
                                   List<RecentViewBadgeDomain> badges, String wholesale,
                                   String preorder, Boolean isWishlist) {
        this.shop = shop;
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUri = imgUri;
        this.isGold = isGold;
        this.labels = labels;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.freeReturn = freeReturn;
        this.badges = badges;
        this.wholesale = wholesale;
        this.preorder = preorder;
        this.isWishlist = isWishlist;
    }

    public RecentViewShopDomain getShop() {
        return shop;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImgUri() {
        return imgUri;
    }

    public String getIsGold() {
        return isGold;
    }

    public List<RecentViewLabelDomain> getLabels() {
        return labels;
    }

    public String getRating() {
        return rating;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public String getFreeReturn() {
        return freeReturn;
    }

    public List<RecentViewBadgeDomain> getBadges() {
        return badges;
    }

    public String getWholesale() {
        return wholesale;
    }

    public String getPreorder() {
        return preorder;
    }

    public Boolean getWishlist() {
        return isWishlist;
    }

    public void setWishlist(Boolean wishlist) {
        isWishlist = wishlist;
    }
}
