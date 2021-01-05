package com.tokopedia.navigation.domain.model;

/**
 * Created by meta on 25/07/18.
 */
public class Notification {

    public int totalNewInbox = 0;
    private Integer totalInbox;
    private Integer totalNotif;
    private Integer totalCart;
    private Boolean isHaveNewFeed;
    private Boolean shouldOsAppear;

    public Integer getTotalInbox() {
        return totalInbox;
    }

    public void setTotalInbox(Integer totalInbox) {
        this.totalInbox = totalInbox;
    }

    public Integer getTotalNotif() {
        return totalNotif;
    }

    public void setTotalNotif(Integer totalNotif) {
        this.totalNotif = totalNotif;
    }

    public Integer getTotalCart() {
        return totalCart;
    }

    public void setTotalCart(Integer totalCart) {
        this.totalCart = totalCart;
    }

    public Boolean getHaveNewFeed() {
        return isHaveNewFeed;
    }

    public void setHaveNewFeed(Boolean haveNewFeed) {
        isHaveNewFeed = haveNewFeed;
    }

    public Boolean getShouldOsAppear() {
        return shouldOsAppear;
    }

    public void setShouldOsAppear(Boolean shouldOsAppear) {
        this.shouldOsAppear = shouldOsAppear;
    }
}
