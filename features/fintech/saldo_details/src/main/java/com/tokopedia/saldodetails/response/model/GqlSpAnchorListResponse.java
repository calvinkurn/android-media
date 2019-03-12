package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class GqlSpAnchorListResponse implements Parcelable {
    @SerializedName("label")
    private String label;

    @SerializedName("url")
    private String url;

    @SerializedName("color")
    private String color;


    protected GqlSpAnchorListResponse(Parcel in) {
        this.label = in.readString();
        this.url = in.readString();
        this.color = in.readString();
    }

    public static final Creator<GqlSpAnchorListResponse> CREATOR = new Creator<GqlSpAnchorListResponse>() {
        @Override
        public GqlSpAnchorListResponse createFromParcel(Parcel in) {
            return new GqlSpAnchorListResponse(in);
        }

        @Override
        public GqlSpAnchorListResponse[] newArray(int size) {
            return new GqlSpAnchorListResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(label);
        dest.writeValue(url);
        dest.writeValue(color);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
