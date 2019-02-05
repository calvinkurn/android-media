package com.tokopedia.notifications.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.notifications.common.CMConstant;

/**
 * @author lalit.singh
 */
public class PersistentButton implements Parcelable {

    @SerializedName(CMConstant.PayloadKeys.APP_LINK)
    String appLink;

    @SerializedName(CMConstant.PayloadKeys.TEXT)
    String text;

    @SerializedName(CMConstant.PayloadKeys.ICON)
    String icon;


    boolean isAppLogo;


    public PersistentButton() {
    }

    protected PersistentButton(Parcel in) {
        appLink = in.readString();
        text = in.readString();
        icon = in.readString();
        isAppLogo = in.readByte() != 0;
    }

    public static final Creator<PersistentButton> CREATOR = new Creator<PersistentButton>() {
        @Override
        public PersistentButton createFromParcel(Parcel in) {
            return new PersistentButton(in);
        }

        @Override
        public PersistentButton[] newArray(int size) {
            return new PersistentButton[size];
        }
    };

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isAppLogo() {
        return isAppLogo;
    }

    public void setAppLogo(boolean appLogo) {
        isAppLogo = appLogo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(appLink);
        parcel.writeString(text);
        parcel.writeString(icon);
        parcel.writeByte((byte) (isAppLogo ? 1 : 0));
    }
}
