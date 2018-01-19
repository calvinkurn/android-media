package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class Rule implements Parcelable {

    private int maximumLength;
    private String productText;
    private int productViewStyle;
    private boolean showPrice;
    private boolean enableVoucher;
    private String buttonText;

    public int getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(int maximumLength) {
        this.maximumLength = maximumLength;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public int getProductViewStyle() {
        return productViewStyle;
    }

    public void setProductViewStyle(int productViewStyle) {
        this.productViewStyle = productViewStyle;
    }

    public boolean isShowPrice() {
        return showPrice;
    }

    public void setShowPrice(boolean showPrice) {
        this.showPrice = showPrice;
    }

    public boolean isEnableVoucher() {
        return enableVoucher;
    }

    public void setEnableVoucher(boolean enableVoucher) {
        this.enableVoucher = enableVoucher;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonText() {
        return buttonText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maximumLength);
        dest.writeString(this.productText);
        dest.writeInt(this.productViewStyle);
        dest.writeByte(this.showPrice ? (byte) 1 : (byte) 0);
        dest.writeByte(this.enableVoucher ? (byte) 1 : (byte) 0);
        dest.writeString(this.buttonText);
    }

    public Rule() {
    }

    protected Rule(Parcel in) {
        this.maximumLength = in.readInt();
        this.productText = in.readString();
        this.productViewStyle = in.readInt();
        this.showPrice = in.readByte() != 0;
        this.enableVoucher = in.readByte() != 0;
        this.buttonText = in.readString();
    }

    public static final Parcelable.Creator<Rule> CREATOR = new Parcelable.Creator<Rule>() {
        @Override
        public Rule createFromParcel(Parcel source) {
            return new Rule(source);
        }

        @Override
        public Rule[] newArray(int size) {
            return new Rule[size];
        }
    };

}
