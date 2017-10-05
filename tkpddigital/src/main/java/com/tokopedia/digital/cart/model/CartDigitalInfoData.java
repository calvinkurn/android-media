package com.tokopedia.digital.cart.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class CartDigitalInfoData implements Parcelable {

    private String type;

    private String id;

    private AttributesDigital attributes;

    private String title;

    private boolean instantCheckout;

    private boolean needOtp;

    private String smsState;

    private List<CartItemDigital> mainInfo;

    private List<CartAdditionalInfo> additionalInfos;

    private Relationships relationships;

    private boolean forceRenderCart;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttributesDigital getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesDigital attributes) {
        this.attributes = attributes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isInstantCheckout() {
        return instantCheckout;
    }

    public void setInstantCheckout(boolean instantCheckout) {
        this.instantCheckout = instantCheckout;
    }

    public boolean isNeedOtp() {
        return needOtp;
    }

    public void setNeedOtp(boolean needOtp) {
        this.needOtp = needOtp;
    }

    public String getSmsState() {
        return smsState;
    }

    public void setSmsState(String smsState) {
        this.smsState = smsState;
    }

    public List<CartItemDigital> getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(List<CartItemDigital> mainInfo) {
        this.mainInfo = mainInfo;
    }

    public List<CartAdditionalInfo> getAdditionalInfos() {
        return additionalInfos;
    }

    public void setAdditionalInfos(List<CartAdditionalInfo> additionalInfos) {
        this.additionalInfos = additionalInfos;
    }

    public Relationships getRelationships() {
        return relationships;
    }

    public void setRelationships(Relationships relationships) {
        this.relationships = relationships;
    }

    public boolean isForceRenderCart() {
        return forceRenderCart;
    }

    public void setForceRenderCart(boolean forceRenderCart) {
        this.forceRenderCart = forceRenderCart;
    }

    public CartDigitalInfoData() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.id);
        dest.writeParcelable(this.attributes, flags);
        dest.writeString(this.title);
        dest.writeByte(this.instantCheckout ? (byte) 1 : (byte) 0);
        dest.writeByte(this.needOtp ? (byte) 1 : (byte) 0);
        dest.writeString(this.smsState);
        dest.writeTypedList(this.mainInfo);
        dest.writeTypedList(this.additionalInfos);
        dest.writeParcelable(this.relationships, flags);
        dest.writeByte(this.forceRenderCart ? (byte) 1 : (byte) 0);
    }

    protected CartDigitalInfoData(Parcel in) {
        this.type = in.readString();
        this.id = in.readString();
        this.attributes = in.readParcelable(AttributesDigital.class.getClassLoader());
        this.title = in.readString();
        this.instantCheckout = in.readByte() != 0;
        this.needOtp = in.readByte() != 0;
        this.smsState = in.readString();
        this.mainInfo = in.createTypedArrayList(CartItemDigital.CREATOR);
        this.additionalInfos = in.createTypedArrayList(CartAdditionalInfo.CREATOR);
        this.relationships = in.readParcelable(Relationships.class.getClassLoader());
        this.forceRenderCart = in.readByte() != 0;
    }

    public static final Creator<CartDigitalInfoData> CREATOR = new Creator<CartDigitalInfoData>() {
        @Override
        public CartDigitalInfoData createFromParcel(Parcel source) {
            return new CartDigitalInfoData(source);
        }

        @Override
        public CartDigitalInfoData[] newArray(int size) {
            return new CartDigitalInfoData[size];
        }
    };
}
