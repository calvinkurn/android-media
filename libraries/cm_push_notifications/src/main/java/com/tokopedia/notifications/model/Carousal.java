package com.tokopedia.notifications.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.notifications.common.CMConstant;

/**
 * @author lalit.singh
 */
public class Carousal implements Parcelable {

    @SerializedName(CMConstant.PayloadKeys.APP_LINK)
    String appLink;

    @SerializedName(CMConstant.PayloadKeys.TEXT)
    String text;

    @SerializedName(CMConstant.PayloadKeys.IMG)
    String icon;

    String filePath;

    int index = 0;

    protected Carousal(Parcel in) {
        appLink = in.readString();
        text = in.readString();
        icon = in.readString();
        filePath = in.readString();
        index = in.readInt();
    }

    public static final Creator<Carousal> CREATOR = new Creator<Carousal>() {
        @Override
        public Carousal createFromParcel(Parcel in) {
            return new Carousal(in);
        }

        @Override
        public Carousal[] newArray(int size) {
            return new Carousal[size];
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appLink);
        dest.writeString(text);
        dest.writeString(icon);
        dest.writeString(filePath);
        dest.writeInt(index);
    }
}
