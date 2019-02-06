package com.tokopedia.checkout.domain.datamodel.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fajar Ulin Nuha on 15/11/18.
 */
public class PurchaseProtectionPlanData implements Parcelable {

    private boolean protectionAvailable;
    private int protectionTypeId;
    private int protectionPricePerProduct;
    private int protectionPrice;
    private String protectionTitle;
    private String protectionSubtitle;
    private String protectionLinkText;
    private String protectionLinkUrl;
    private boolean protectionOptIn;

    public PurchaseProtectionPlanData() {
    }

    public boolean isProtectionAvailable() {
        return protectionAvailable;
    }

    public void setProtectionAvailable(boolean protectionAvailable) {
        this.protectionAvailable = protectionAvailable;
    }

    public int getProtectionTypeId() {
        return protectionTypeId;
    }

    public void setProtectionTypeId(int protectionTypeId) {
        this.protectionTypeId = protectionTypeId;
    }

    public int getProtectionPricePerProduct() {
        return protectionPricePerProduct;
    }

    public void setProtectionPricePerProduct(int protectionPricePerProduct) {
        this.protectionPricePerProduct = protectionPricePerProduct;
    }

    public int getProtectionPrice() {
        return protectionPrice;
    }

    public void setProtectionPrice(int protectionPrice) {
        this.protectionPrice = protectionPrice;
    }

    public String getProtectionTitle() {
        return protectionTitle;
    }

    public void setProtectionTitle(String protectionTitle) {
        this.protectionTitle = protectionTitle;
    }

    public String getProtectionSubtitle() {
        return protectionSubtitle;
    }

    public void setProtectionSubtitle(String protectionSubtitle) {
        this.protectionSubtitle = protectionSubtitle;
    }

    public String getProtectionLinkText() {
        return protectionLinkText;
    }

    public void setProtectionLinkText(String protectionLinkText) {
        this.protectionLinkText = protectionLinkText;
    }

    public String getProtectionLinkUrl() {
        return protectionLinkUrl;
    }

    public void setProtectionLinkUrl(String protectionLinkUrl) {
        this.protectionLinkUrl = protectionLinkUrl;
    }

    public boolean isProtectionOptIn() {
        return protectionOptIn;
    }

    public void setProtectionOptIn(boolean protectionOptIn) {
        this.protectionOptIn = protectionOptIn;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.protectionAvailable ? (byte) 1 : (byte) 0);
        dest.writeInt(this.protectionTypeId);
        dest.writeInt(this.protectionPricePerProduct);
        dest.writeInt(this.protectionPrice);
        dest.writeString(this.protectionTitle);
        dest.writeString(this.protectionSubtitle);
        dest.writeString(this.protectionLinkText);
        dest.writeString(this.protectionLinkUrl);
        dest.writeByte(this.protectionOptIn ? (byte) 1 : (byte) 0);
    }

    protected PurchaseProtectionPlanData(Parcel in) {
        this.protectionAvailable = in.readByte() != 0;
        this.protectionTypeId = in.readInt();
        this.protectionPricePerProduct = in.readInt();
        this.protectionPrice = in.readInt();
        this.protectionTitle = in.readString();
        this.protectionSubtitle = in.readString();
        this.protectionLinkText = in.readString();
        this.protectionLinkUrl = in.readString();
        this.protectionOptIn = in.readByte() != 0;
    }

    public static final Parcelable.Creator<PurchaseProtectionPlanData> CREATOR = new Parcelable.Creator<PurchaseProtectionPlanData>() {
        @Override
        public PurchaseProtectionPlanData createFromParcel(Parcel source) {
            return new PurchaseProtectionPlanData(source);
        }

        @Override
        public PurchaseProtectionPlanData[] newArray(int size) {
            return new PurchaseProtectionPlanData[size];
        }
    };
}
