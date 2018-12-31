package com.tokopedia.core.product.model.productdetail;

/**
 * Created by stevenfredian on 7/18/16.
 */


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class ReturnInfo implements Parcelable{
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("color_hex")
    @Expose
    private String colorHex;
    @SerializedName("color_rgb")
    @Expose
    private String colorRGB;
    @SerializedName("content")
    @Expose
    private String content;


    public String getColorRGB() {
        return colorRGB;
    }

    public void setColorRGB(String colorRGB) {
        this.colorRGB = colorRGB;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    /**
     * @return The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }


    /**
     * @return The content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    public ReturnInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.icon);
        dest.writeString(this.colorHex);
        dest.writeString(this.colorRGB);
        dest.writeString(this.content);
    }

    protected ReturnInfo(Parcel in) {
        this.icon = in.readString();
        this.colorHex = in.readString();
        this.colorRGB = in.readString();
        this.content = in.readString();
    }

    public static final Creator<ReturnInfo> CREATOR = new Creator<ReturnInfo>() {
        @Override
        public ReturnInfo createFromParcel(Parcel source) {
            return new ReturnInfo(source);
        }

        @Override
        public ReturnInfo[] newArray(int size) {
            return new ReturnInfo[size];
        }
    };
}