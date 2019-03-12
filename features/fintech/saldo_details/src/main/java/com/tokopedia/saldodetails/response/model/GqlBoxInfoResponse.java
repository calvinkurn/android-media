package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GqlBoxInfoResponse implements Parcelable {


    @SerializedName("box_title")
    private String boxTitle;

    @SerializedName("box_desc")
    private String boxDesc;

    @SerializedName("box_bg_color")
    private String boxBgColor;

    @SerializedName("link_text")
    private String linkText;

    @SerializedName("link_url")
    private String linkUrl;

    @SerializedName("link_text_color")
    private String linkTextColor;

    public static final Creator<GqlBoxInfoResponse> CREATOR = new Creator<GqlBoxInfoResponse>() {
        @Override
        public GqlBoxInfoResponse createFromParcel(Parcel in) {
            return new GqlBoxInfoResponse(in);
        }

        @Override
        public GqlBoxInfoResponse[] newArray(int size) {
            return new GqlBoxInfoResponse[size];
        }
    };


    protected GqlBoxInfoResponse(Parcel in) {
        this.boxTitle = in.readString();
        this.boxDesc = in.readString();
        this.boxBgColor = in.readString();
        this.linkText = in.readString();
        this.linkUrl = in.readString();
        this.linkTextColor = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boxTitle);
        dest.writeString(boxDesc);
        dest.writeString(boxBgColor);
        dest.writeString(linkText);
        dest.writeString(linkUrl);
        dest.writeString(linkTextColor);
    }

    public String getBoxTitle() {
        return boxTitle;
    }

    public void setBoxTitle(String boxTitle) {
        this.boxTitle = boxTitle;
    }

    public String getBoxDesc() {
        return boxDesc;
    }

    public void setBoxDesc(String boxDesc) {
        this.boxDesc = boxDesc;
    }

    public String getBoxBgColor() {
        return boxBgColor;
    }

    public void setBoxBgColor(String boxBgColor) {
        this.boxBgColor = boxBgColor;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkTextColor() {
        return linkTextColor;
    }

    public void setLinkTextColor(String linkTextColor) {
        this.linkTextColor = linkTextColor;
    }
}
