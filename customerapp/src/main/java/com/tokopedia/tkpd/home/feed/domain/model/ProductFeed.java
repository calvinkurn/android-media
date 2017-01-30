package com.tokopedia.tkpd.home.feed.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kulomady on 12/8/16.
 */
public class ProductFeed extends Product {
    private Shop mShop;
    private String freeReturn;
    private List<Badge> badges = new ArrayList<>();
    private String wholesale;
    private String preorder;

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

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public Shop getShop() {
        return mShop;
    }

    public void setShop(Shop shop) {
        mShop = shop;
    }

    public String getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(String freeReturn) {
        this.freeReturn = freeReturn;
    }
}
