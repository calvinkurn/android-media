package com.tokopedia.core.drawer2.data.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 1/24/17.
 */

public class DrawerTokoCash implements Parcelable {

    @Deprecated
    private DrawerTokoCashAction drawerTokoCashAction;

    private HomeHeaderWalletAction homeHeaderWalletAction;
    private DrawerWalletAction drawerWalletAction;

    public DrawerTokoCash() {
    }

    public DrawerWalletAction getDrawerWalletAction() {
        return drawerWalletAction;
    }

    public void setDrawerWalletAction(DrawerWalletAction drawerWalletAction) {
        this.drawerWalletAction = drawerWalletAction;
    }

    @Deprecated
    public DrawerTokoCashAction getDrawerTokoCashAction() {
        return drawerTokoCashAction;
    }

    @Deprecated
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
        dest.writeParcelable(this.drawerWalletAction, flags);
    }

    protected DrawerTokoCash(Parcel in) {
        this.drawerTokoCashAction = in.readParcelable(DrawerTokoCashAction.class.getClassLoader());
        this.homeHeaderWalletAction = in.readParcelable(HomeHeaderWalletAction.class.getClassLoader());
        this.drawerWalletAction = in.readParcelable(DrawerWalletAction.class.getClassLoader());
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
