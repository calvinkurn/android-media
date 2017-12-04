package com.tokopedia.digital.widget.model.operator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class Rule implements Parcelable {

    private String productText;
    private int productsViewStyle;
    private boolean showPrice;
    private boolean showProduct;
    private boolean showProductListPage;
    private boolean allowAphanumericNumber;
    private String buttonLabel;

    public Rule() {
    }

    protected Rule(Parcel in) {
        productText = in.readString();
        productsViewStyle = in.readInt();
        showPrice = in.readByte() != 0;
        showProduct = in.readByte() != 0;
        showProductListPage = in.readByte() != 0;
        allowAphanumericNumber = in.readByte() != 0;
        buttonLabel = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productText);
        dest.writeInt(productsViewStyle);
        dest.writeByte((byte) (showPrice ? 1 : 0));
        dest.writeByte((byte) (showProduct ? 1 : 0));
        dest.writeByte((byte) (showProductListPage ? 1 : 0));
        dest.writeByte((byte) (allowAphanumericNumber ? 1 : 0));
        dest.writeString(buttonLabel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Rule> CREATOR = new Creator<Rule>() {
        @Override
        public Rule createFromParcel(Parcel in) {
            return new Rule(in);
        }

        @Override
        public Rule[] newArray(int size) {
            return new Rule[size];
        }
    };

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public int getProductsViewStyle() {
        return productsViewStyle;
    }

    public void setProductsViewStyle(int productsViewStyle) {
        this.productsViewStyle = productsViewStyle;
    }

    public boolean isShowPrice() {
        return showPrice;
    }

    public void setShowPrice(boolean showPrice) {
        this.showPrice = showPrice;
    }

    public boolean isShowProduct() {
        return showProduct;
    }

    public void setShowProduct(boolean showProduct) {
        this.showProduct = showProduct;
    }

    public boolean isShowProductListPage() {
        return showProductListPage;
    }

    public void setShowProductListPage(boolean showProductListPage) {
        this.showProductListPage = showProductListPage;
    }

    public boolean isAllowAphanumericNumber() {
        return allowAphanumericNumber;
    }

    public void setAllowAphanumericNumber(boolean allowAphanumericNumber) {
        this.allowAphanumericNumber = allowAphanumericNumber;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }
}