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

    protected DrawerTokoCashAction(Parcel in) {
        text = in.readString();
        redirectUrl = in.readString();
    }

    public static final Creator<DrawerTokoCashAction> CREATOR = new Creator<DrawerTokoCashAction>() {
        @Override
        public DrawerTokoCashAction createFromParcel(Parcel in) {
            return new DrawerTokoCashAction(in);
        }

        @Override
        public DrawerTokoCashAction[] newArray(int size) {
            return new DrawerTokoCashAction[size];
        }
    };

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
        dest.writeString(text);
        dest.writeString(redirectUrl);
    }
}
