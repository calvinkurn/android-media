package com.tokopedia.logisticcart.domain.shipping;

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
    private String warehouseId;
    private String productWeight;
    private String promoCode;
    private String promoDetails;
    private String buyerAddressId;
    private String shippingDuration;
    private String courier;
    private String shippingPrice;
    private String codFlag;
    private String tokopediaCornerFlag;
    private String isFulfillment;

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

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getPromoDetails() {
        return promoDetails;
    }

    public void setPromoDetails(String promoDetails) {
        this.promoDetails = promoDetails;
    }

    public String getBuyerAddressId() {
        return buyerAddressId;
    }

    public void setBuyerAddressId(String buyerAddressId) {
        this.buyerAddressId = buyerAddressId;
    }

    public String getShippingDuration() {
        return shippingDuration;
    }

    public void setShippingDuration(String shippingDuration) {
        this.shippingDuration = shippingDuration;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(String shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getCodFlag() {
        return codFlag;
    }

    public void setCodFlag(String codFlag) {
        this.codFlag = codFlag;
    }

    public String getTokopediaCornerFlag() {
        return tokopediaCornerFlag;
    }

    public void setTokopediaCornerFlag(String tokopediaCornerFlag) {
        this.tokopediaCornerFlag = tokopediaCornerFlag;
    }

    public String getIsFulfillment() {
        return isFulfillment;
    }

    public void setIsFulfillment(String isFulfillment) {
        this.isFulfillment = isFulfillment;
    }

    public AnalyticsProductCheckoutData() {
    }

    protected AnalyticsProductCheckoutData(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        productPrice = in.readString();
        productBrand = in.readString();
        productCategory = in.readString();
        productVariant = in.readString();
        productQuantity = in.readInt();
        productShopId = in.readString();
        productShopType = in.readString();
        productShopName = in.readString();
        productCategoryId = in.readString();
        productListName = in.readString();
        productAttribution = in.readString();
        warehouseId = in.readString();
        productWeight = in.readString();
        promoCode = in.readString();
        promoDetails = in.readString();
        buyerAddressId = in.readString();
        shippingDuration = in.readString();
        courier = in.readString();
        shippingPrice = in.readString();
        codFlag = in.readString();
        tokopediaCornerFlag = in.readString();
        isFulfillment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(productPrice);
        dest.writeString(productBrand);
        dest.writeString(productCategory);
        dest.writeString(productVariant);
        dest.writeInt(productQuantity);
        dest.writeString(productShopId);
        dest.writeString(productShopType);
        dest.writeString(productShopName);
        dest.writeString(productCategoryId);
        dest.writeString(productListName);
        dest.writeString(productAttribution);
        dest.writeString(warehouseId);
        dest.writeString(productWeight);
        dest.writeString(promoCode);
        dest.writeString(promoDetails);
        dest.writeString(buyerAddressId);
        dest.writeString(shippingDuration);
        dest.writeString(courier);
        dest.writeString(shippingPrice);
        dest.writeString(codFlag);
        dest.writeString(tokopediaCornerFlag);
        dest.writeString(isFulfillment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnalyticsProductCheckoutData> CREATOR = new Creator<AnalyticsProductCheckoutData>() {
        @Override
        public AnalyticsProductCheckoutData createFromParcel(Parcel in) {
            return new AnalyticsProductCheckoutData(in);
        }

        @Override
        public AnalyticsProductCheckoutData[] newArray(int size) {
            return new AnalyticsProductCheckoutData[size];
        }
    };

}
