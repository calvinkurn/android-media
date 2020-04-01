package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/7/17.
 */
public class OrderProductViewModel implements Parcelable{

    private int orderDeliverQuantity;
    private int productWeightUnit;
    private int orderDetailId;
    private String productStatus;
    private int productId;
    private String productCurrentWeight;
    private String productPicture;
    private String productPrice;
    private String productDescription;
    private String productNormalPrice;
    private String productPriceCurrency;
    private String productNotes;
    private String orderSubtotalPrice;
    private int productQuantity;
    private String productWeight;
    private String orderSubtotalPriceIdr;
    private int productRejectQuantity;
    private String productUrl;
    private String productName;

    public OrderProductViewModel() {
    }

    protected OrderProductViewModel(Parcel in) {
        orderDeliverQuantity = in.readInt();
        productWeightUnit = in.readInt();
        orderDetailId = in.readInt();
        productStatus = in.readString();
        productId = in.readInt();
        productCurrentWeight = in.readString();
        productPicture = in.readString();
        productPrice = in.readString();
        productDescription = in.readString();
        productNormalPrice = in.readString();
        productPriceCurrency = in.readString();
        productNotes = in.readString();
        orderSubtotalPrice = in.readString();
        productQuantity = in.readInt();
        productWeight = in.readString();
        orderSubtotalPriceIdr = in.readString();
        productRejectQuantity = in.readInt();
        productUrl = in.readString();
        productName = in.readString();
    }

    public static final Creator<OrderProductViewModel> CREATOR = new Creator<OrderProductViewModel>() {
        @Override
        public OrderProductViewModel createFromParcel(Parcel in) {
            return new OrderProductViewModel(in);
        }

        @Override
        public OrderProductViewModel[] newArray(int size) {
            return new OrderProductViewModel[size];
        }
    };

    public int getOrderDeliverQuantity() {
        return orderDeliverQuantity;
    }

    public void setOrderDeliverQuantity(int orderDeliverQuantity) {
        this.orderDeliverQuantity = orderDeliverQuantity;
    }

    public int getProductWeightUnit() {
        return productWeightUnit;
    }

    public void setProductWeightUnit(int productWeightUnit) {
        this.productWeightUnit = productWeightUnit;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductCurrentWeight() {
        return productCurrentWeight;
    }

    public void setProductCurrentWeight(String productCurrentWeight) {
        this.productCurrentWeight = productCurrentWeight;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductNormalPrice() {
        return productNormalPrice;
    }

    public void setProductNormalPrice(String productNormalPrice) {
        this.productNormalPrice = productNormalPrice;
    }

    public String getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public void setProductPriceCurrency(String productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public String getOrderSubtotalPrice() {
        return orderSubtotalPrice;
    }

    public void setOrderSubtotalPrice(String orderSubtotalPrice) {
        this.orderSubtotalPrice = orderSubtotalPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getOrderSubtotalPriceIdr() {
        return orderSubtotalPriceIdr;
    }

    public void setOrderSubtotalPriceIdr(String orderSubtotalPriceIdr) {
        this.orderSubtotalPriceIdr = orderSubtotalPriceIdr;
    }

    public int getProductRejectQuantity() {
        return productRejectQuantity;
    }

    public void setProductRejectQuantity(int productRejectQuantity) {
        this.productRejectQuantity = productRejectQuantity;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderDeliverQuantity);
        dest.writeInt(productWeightUnit);
        dest.writeInt(orderDetailId);
        dest.writeString(productStatus);
        dest.writeInt(productId);
        dest.writeString(productCurrentWeight);
        dest.writeString(productPicture);
        dest.writeString(productPrice);
        dest.writeString(productDescription);
        dest.writeString(productNormalPrice);
        dest.writeString(productPriceCurrency);
        dest.writeString(productNotes);
        dest.writeString(orderSubtotalPrice);
        dest.writeInt(productQuantity);
        dest.writeString(productWeight);
        dest.writeString(orderSubtotalPriceIdr);
        dest.writeInt(productRejectQuantity);
        dest.writeString(productUrl);
        dest.writeString(productName);
    }
}
