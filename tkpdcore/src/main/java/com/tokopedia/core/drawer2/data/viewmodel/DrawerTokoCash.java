package com.tokopedia.core.drawer2.data.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 1/24/17.
 */

public class DrawerTokoCash implements Parcelable {

    private DrawerTokoCashAction drawerTokoCashAction;

    private HomeHeaderWalletAction homeHeaderWalletAction;

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

    public HomeHeaderWalletAction getHomeHeaderWalletAction() {
        return homeHeaderWalletAction;
    }

    public void setHomeHeaderWalletAction(HomeHeaderWalletAction homeHeaderWalletAction) {
        this.homeHeaderWalletAction = homeHeaderWalletAction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.drawerTokoCashAction, flags);
        dest.writeParcelable(this.homeHeaderWalletAction, flags);
        dest.writeString(this.balance);
        dest.writeString(this.redirectUrl);
        dest.writeString(this.text);
        dest.writeInt(this.link);
    }

    protected DrawerTokoCash(Parcel in) {
        this.drawerTokoCashAction = in.readParcelable(DrawerTokoCashAction.class.getClassLoader());
        this.homeHeaderWalletAction = in.readParcelable(HomeHeaderWalletAction.class.getClassLoader());
        this.balance = in.readString();
        this.redirectUrl = in.readString();
        this.text = in.readString();
        this.link = in.readInt();
    }

    public static final Creator<DrawerTokoCash> CREATOR = new Creator<DrawerTokoCash>() {
        @Override
        public DrawerTokoCash createFromParcel(Parcel source) {
            return new DrawerTokoCash(source);
        }

        @Override
        public DrawerTokoCash[] newArray(int size) {
            return new DrawerTokoCash[size];
        }
    };
}
