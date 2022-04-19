package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OntimeDeliveryGuarantee implements Parcelable {

    @SerializedName("available")
    @Expose
    private boolean available;
    @SerializedName("text_label")
    @Expose
    private String textLabel;
    @SerializedName("text_detail")
    @Expose
    private String textDetail;
    @SerializedName("url_detail")
    @Expose
    private String urlDetail;
    @SerializedName("value")
    @Expose
    private int value;
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getTextLabel() {
        return textLabel;
    }

    public void setTextLabel(String textLabel) {
        this.textLabel = textLabel;
    }

    public String getTextDetail() {
        return textDetail;
    }

    public void setTextDetail(String textDetail) {
        this.textDetail = textDetail;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getUrlDetail() {
        return urlDetail;
    }

    public void setUrlDetail(String urlDetail) {
        this.urlDetail = urlDetail;
    }

    public OntimeDeliveryGuarantee() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.available ? (byte) 1 : (byte) 0);
        dest.writeString(this.textLabel);
        dest.writeString(this.textDetail);
        dest.writeString(this.urlDetail);
        dest.writeInt(this.value);
        dest.writeString(this.iconUrl);
    }

    protected OntimeDeliveryGuarantee(Parcel in) {
        this.available = in.readByte() != 0;
        this.textLabel = in.readString();
        this.textDetail = in.readString();
        this.urlDetail = in.readString();
        this.value = in.readInt();
        this.iconUrl = in.readString();
    }

    public static final Creator<OntimeDeliveryGuarantee> CREATOR = new Creator<OntimeDeliveryGuarantee>() {
        @Override
        public OntimeDeliveryGuarantee createFromParcel(Parcel source) {
            return new OntimeDeliveryGuarantee(source);
        }

        @Override
        public OntimeDeliveryGuarantee[] newArray(int size) {
            return new OntimeDeliveryGuarantee[size];
        }
    };
}
