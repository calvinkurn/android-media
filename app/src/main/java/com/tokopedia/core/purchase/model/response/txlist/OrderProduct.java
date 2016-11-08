package com.tokopedia.core.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class OrderProduct implements Parcelable {
    private static final String TAG = OrderProduct.class.getSimpleName();

    @SerializedName("order_deliver_quantity")
    @Expose
    private String orderDeliverQuantity;
    @SerializedName("product_picture")
    @Expose
    private String productPicture;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("order_detail_id")
    @Expose
    private String orderDetailId;
    @SerializedName("product_notes")
    @Expose
    private String productNotes;
    @SerializedName("product_status")
    @Expose
    private String productStatus;
    @SerializedName("order_subtotal_price")
    @Expose
    private String orderSubtotalPrice;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_quantity")
    @Expose
    private String productQuantity;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("order_subtotal_price_idr")
    @Expose
    private String orderSubtotalPriceIdr;
    @SerializedName("product_reject_quantity")
    @Expose
    private Integer productRejectQuantity;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_url")
    @Expose
    private String productUrl;


    public String getOrderDeliverQuantity() {
        return orderDeliverQuantity;
    }

    public void setOrderDeliverQuantity(String orderDeliverQuantity) {
        this.orderDeliverQuantity = orderDeliverQuantity;
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

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getOrderSubtotalPrice() {
        return orderSubtotalPrice;
    }

    public void setOrderSubtotalPrice(String orderSubtotalPrice) {
        this.orderSubtotalPrice = orderSubtotalPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
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

    public Integer getProductRejectQuantity() {
        return productRejectQuantity;
    }

    public void setProductRejectQuantity(Integer productRejectQuantity) {
        this.productRejectQuantity = productRejectQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    protected OrderProduct(Parcel in) {
        orderDeliverQuantity = in.readString();
        productPicture = in.readString();
        productPrice = in.readString();
        orderDetailId = in.readString();
        productNotes = in.readString();
        productStatus = in.readString();
        orderSubtotalPrice = in.readString();
        productId = in.readString();
        productQuantity = in.readString();
        productWeight = in.readString();
        orderSubtotalPriceIdr = in.readString();
        productRejectQuantity = in.readByte() == 0x00 ? null : in.readInt();
        productName = in.readString();
        productUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderDeliverQuantity);
        dest.writeString(productPicture);
        dest.writeString(productPrice);
        dest.writeString(orderDetailId);
        dest.writeString(productNotes);
        dest.writeString(productStatus);
        dest.writeString(orderSubtotalPrice);
        dest.writeString(productId);
        dest.writeString(productQuantity);
        dest.writeString(productWeight);
        dest.writeString(orderSubtotalPriceIdr);
        if (productRejectQuantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productRejectQuantity);
        }
        dest.writeString(productName);
        dest.writeString(productUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OrderProduct> CREATOR = new Parcelable.Creator<OrderProduct>() {
        @Override
        public OrderProduct createFromParcel(Parcel in) {
            return new OrderProduct(in);
        }

        @Override
        public OrderProduct[] newArray(int size) {
            return new OrderProduct[size];
        }
    };
}
