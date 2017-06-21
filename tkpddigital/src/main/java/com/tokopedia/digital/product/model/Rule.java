package com.tokopedia.digital.product.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class Rule implements Parcelable {


    private String productText;
    private int productViewStyle;
    private boolean showPrice;
    private boolean enableVoucher;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productText);
        dest.writeInt(this.productViewStyle);
        dest.writeByte(this.showPrice ? (byte) 1 : (byte) 0);
        dest.writeByte(this.enableVoucher ? (byte) 1 : (byte) 0);
    }

    public Rule() {
    }

    protected Rule(Parcel in) {
        this.productText = in.readString();
        this.productViewStyle = in.readInt();
        this.showPrice = in.readByte() != 0;
        this.enableVoucher = in.readByte() != 0;
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
