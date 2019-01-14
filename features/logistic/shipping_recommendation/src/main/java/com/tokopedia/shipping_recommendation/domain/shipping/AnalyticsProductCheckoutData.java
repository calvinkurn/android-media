package com.tokopedia.shipping_recommendation.domain.shipping;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 01/08/18.
 */
public class AnalyticsProductCheckoutData implements Parcelable {

    private String productId;
    private String productName;
    private String productPrice;
    private String productBrand;
    private String productCategory;
    private String productVariant;
    private int productQuantity;
    private String productShopId;
    private String productShopType;
    private String productShopName;
    private String productCategoryId;
    private String productListName;
    private String productAttribution;

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductVariant() {
        return productVariant;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public String getProductShopId() {
        return productShopId;
    }

    public String getProductShopType() {
        return productShopType;
    }

    public String getProductShopName() {
        return productShopName;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public String getProductListName() {
        return productListName;
    }

    public String getProductAttribution() {
        return productAttribution;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public void setProductVariant(String productVariant) {
        this.productVariant = productVariant;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setProductShopId(String productShopId) {
        this.productShopId = productShopId;
    }

    public void setProductShopType(String productShopType) {
        this.productShopType = productShopType;
    }

    public void setProductShopName(String productShopName) {
        this.productShopName = productShopName;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public void setProductListName(String productListName) {
        this.productListName = productListName;
    }

    public void setProductAttribution(String productAttribution) {
        this.productAttribution = productAttribution;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.productPrice);
        dest.writeString(this.productBrand);
        dest.writeString(this.productCategory);
        dest.writeString(this.productVariant);
        dest.writeInt(this.productQuantity);
        dest.writeString(this.productShopId);
        dest.writeString(this.productShopType);
        dest.writeString(this.productShopName);
        dest.writeString(this.productCategoryId);
        dest.writeString(this.productListName);
        dest.writeString(this.productAttribution);
    }

    public AnalyticsProductCheckoutData() {
    }

    protected AnalyticsProductCheckoutData(Parcel in) {
        this.productId = in.readString();
        this.productName = in.readString();
        this.productPrice = in.readString();
        this.productBrand = in.readString();
        this.productCategory = in.readString();
        this.productVariant = in.readString();
        this.productQuantity = in.readInt();
        this.productShopId = in.readString();
        this.productShopType = in.readString();
        this.productShopName = in.readString();
        this.productCategoryId = in.readString();
        this.productListName = in.readString();
        this.productAttribution = in.readString();
    }

    public static final Parcelable.Creator<AnalyticsProductCheckoutData> CREATOR = new Parcelable.Creator<AnalyticsProductCheckoutData>() {
        @Override
        public AnalyticsProductCheckoutData createFromParcel(Parcel source) {
            return new AnalyticsProductCheckoutData(source);
        }

        @Override
        public AnalyticsProductCheckoutData[] newArray(int size) {
            return new AnalyticsProductCheckoutData[size];
        }
    };


}
