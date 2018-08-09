package com.tokopedia.navigation.domain.model;

/**
 * Created by meta on 25/07/18.
 */
public class Notification {

    private Integer totalInbox;
    private Integer totalNotif;
    private Integer totalCart;

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
}
