package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 5/10/17.
 */
public class OrderClientNumber implements Parcelable {
    private String clientNumber;
    private String name;
    private String productId;
    private String categoryId;
    private String operatorId;

    private OrderClientNumber(Builder builder) {
        setClientNumber(builder.clientNumber);
        setName(builder.name);
        setProductId(builder.productId);
        setCategoryId(builder.categoryId);
        setOperatorId(builder.operatorId);
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return clientNumber;
    }

    public OrderClientNumber() {
    }


    public static final class Builder {
        private String clientNumber;
        private String name;
        private String productId;
        private String categoryId;
        private String operatorId;

        public Builder() {
        }

        public Builder clientNumber(String val) {
            clientNumber = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder productId(String val) {
            productId = val;
            return this;
        }

        public Builder categoryId(String val) {
            categoryId = val;
            return this;
        }

        public Builder operatorId(String val) {
            operatorId = val;
            return this;
        }

        public OrderClientNumber build() {
            return new OrderClientNumber(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.clientNumber);
        dest.writeString(this.name);
        dest.writeString(this.productId);
        dest.writeString(this.categoryId);
        dest.writeString(this.operatorId);
    }

    protected OrderClientNumber(Parcel in) {
        this.clientNumber = in.readString();
        this.name = in.readString();
        this.productId = in.readString();
        this.categoryId = in.readString();
        this.operatorId = in.readString();
    }

    public static final Creator<OrderClientNumber> CREATOR = new Creator<OrderClientNumber>() {
        @Override
        public OrderClientNumber createFromParcel(Parcel source) {
            return new OrderClientNumber(source);
        }

        @Override
        public OrderClientNumber[] newArray(int size) {
            return new OrderClientNumber[size];
        }
    };
}
