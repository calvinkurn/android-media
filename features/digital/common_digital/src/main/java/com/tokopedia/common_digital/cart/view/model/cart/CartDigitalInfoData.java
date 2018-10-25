package com.tokopedia.common_digital.cart.view.model.cart;

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

    private int crossSellingType;

    private CrossSellingConfig crossSellingConfig;

    protected CartDigitalInfoData(Parcel in) {
        type = in.readString();
        id = in.readString();
        attributes = in.readParcelable(AttributesDigital.class.getClassLoader());
        title = in.readString();
        instantCheckout = in.readByte() != 0;
        needOtp = in.readByte() != 0;
        smsState = in.readString();
        mainInfo = in.createTypedArrayList(CartItemDigital.CREATOR);
        additionalInfos = in.createTypedArrayList(CartAdditionalInfo.CREATOR);
        relationships = in.readParcelable(Relationships.class.getClassLoader());
        forceRenderCart = in.readByte() != 0;
        crossSellingType = in.readInt();
        crossSellingConfig = in.readParcelable(CrossSellingConfig.class.getClassLoader());
    }

    public static final Creator<CartDigitalInfoData> CREATOR = new Creator<CartDigitalInfoData>() {
        @Override
        public CartDigitalInfoData createFromParcel(Parcel in) {
            return new CartDigitalInfoData(in);
        }

        @Override
        public CartDigitalInfoData[] newArray(int size) {
            return new CartDigitalInfoData[size];
        }
    };

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

    public int getCrossSellingType() {
        return crossSellingType;
    }

    public void setCrossSellingType(int crossSellingType) {
        this.crossSellingType = crossSellingType;
    }

    public CrossSellingConfig getCrossSellingConfig() {
        return crossSellingConfig;
    }

    public void setCrossSellingConfig(CrossSellingConfig crossSellingConfig) {
        this.crossSellingConfig = crossSellingConfig;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(id);
        dest.writeParcelable(attributes, flags);
        dest.writeString(title);
        dest.writeByte((byte) (instantCheckout ? 1 : 0));
        dest.writeByte((byte) (needOtp ? 1 : 0));
        dest.writeString(smsState);
        dest.writeTypedList(mainInfo);
        dest.writeTypedList(additionalInfos);
        dest.writeParcelable(relationships, flags);
        dest.writeByte((byte) (forceRenderCart ? 1 : 0));
        dest.writeInt(crossSellingType);
        dest.writeParcelable(crossSellingConfig, flags);
    }
}
