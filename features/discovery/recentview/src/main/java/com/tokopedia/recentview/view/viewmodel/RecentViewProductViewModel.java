package com.tokopedia.recentview.view.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 7/3/17.
 */

public class RecentViewProductViewModel {
    private String id;

    private String name;

    private String price;

    private String shop;

    private String imgUri;

    private int shopId;

    private String preorder;

    private String wholesale;

    private List<BadgeViewModel> badges = new ArrayList<>();

    private String free_return;

    private Boolean isWishlist = false;

    private String isGold;

    public RecentViewProductViewModel(String id, String name, String price, String shop,
                                      String imgUri, int shopId, String preorder, String wholesale,
                                      List<BadgeViewModel> badges, String free_return,
                                      Boolean isWishlist, String isGold) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.shop = shop;
        this.imgUri = imgUri;
        this.shopId = shopId;
        this.preorder = preorder;
        this.wholesale = wholesale;
        this.badges = badges;
        this.free_return = free_return;
        this.isWishlist = isWishlist;
        this.isGold = isGold;
    }

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

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getImgUri() {
        return imgUri;
    }

    public String getIsGold() {
        return isGold;
    }

    public void setIsGold(String isGold) {
        this.isGold = isGold;
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

    public List<BadgeViewModel> getBadges() {
        return badges;
    }

    public void setBadges(List<BadgeViewModel> badges) {
        this.badges = badges;
    }

    public String getFree_return() {
        return free_return;
    }

    public void setFree_return(String free_return) {
        this.free_return = free_return;
    }

    public Boolean getWishlist() {
        return isWishlist;
    }

    public void setWishlist(Boolean wishlist) {
        isWishlist = wishlist;
    }

}
