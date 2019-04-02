package com.tokopedia.core.drawer2.data.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 5/5/17.
 */

public class DrawerTokoCashAction implements Parcelable {

    private String text;
    private String redirectUrl;

    public DrawerTokoCashAction() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.redirectUrl);
    }

    protected DrawerTokoCashAction(Parcel in) {
        this.text = in.readString();
        this.redirectUrl = in.readString();
    }

    public static final Creator<DrawerTokoCashAction> CREATOR = new Creator<DrawerTokoCashAction>() {
        @Override
        public DrawerTokoCashAction createFromParcel(Parcel source) {
            return new DrawerTokoCashAction(source);
        }

        @Override
        public DrawerTokoCashAction[] newArray(int size) {
            return new DrawerTokoCashAction[size];
        }
    };
}
