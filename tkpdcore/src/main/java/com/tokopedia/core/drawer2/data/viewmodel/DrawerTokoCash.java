package com.tokopedia.core.drawer2.data.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 1/24/17.
 */

public class DrawerTokoCash implements Parcelable {

    private DrawerTokoCashAction drawerTokoCashAction;

    private String balance;

    private String redirectUrl;

    private String text;

    private int link;

    public DrawerTokoCash() {
    }

    protected DrawerTokoCash(Parcel in) {
        drawerTokoCashAction = in.readParcelable(DrawerTokoCashAction.class.getClassLoader());
        balance = in.readString();
        redirectUrl = in.readString();
        text = in.readString();
        link = in.readInt();
    }

    public static final Creator<DrawerTokoCash> CREATOR = new Creator<DrawerTokoCash>() {
        @Override
        public DrawerTokoCash createFromParcel(Parcel in) {
            return new DrawerTokoCash(in);
        }

        @Override
        public DrawerTokoCash[] newArray(int size) {
            return new DrawerTokoCash[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(drawerTokoCashAction, flags);
        dest.writeString(balance);
        dest.writeString(redirectUrl);
        dest.writeString(text);
        dest.writeInt(link);
    }
}
