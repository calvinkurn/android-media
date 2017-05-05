package com.tokopedia.core.drawer2.data.viewmodel;

/**
 * Created by nisie on 1/24/17.
 */

public class DrawerTokoCash {

    private DrawerTokoCashAction drawerTokoCashAction;

    private String balance;

    private String redirectUrl;

    private String text;

    private int link;

    public DrawerTokoCash() {
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

    public DrawerTokoCashAction getDrawerTokoCashAction() {
        return drawerTokoCashAction;
    }

    public void setDrawerTokoCashAction(DrawerTokoCashAction drawerTokoCashAction) {
        this.drawerTokoCashAction = drawerTokoCashAction;
    }
}
