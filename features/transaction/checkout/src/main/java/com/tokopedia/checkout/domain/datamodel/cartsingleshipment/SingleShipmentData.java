package com.tokopedia.checkout.domain.datamodel.cartsingleshipment;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 09/03/18.
 */

public class SingleShipmentData implements Parcelable {

    private boolean isError;
    private String errorMessage;
    private boolean isWarning;
    private String warningMessage;

    private RecipientAddressModel recipientAddress;
    private List<CartSellerItemModel> cartItem = new ArrayList<>();
    private ShipmentCostModel shipmentCost;

    private SingleShipmentData(Builder builder) {
        recipientAddress = builder.recipientAddress;
        cartItem = builder.cartItem;
        shipmentCost = builder.shipmentCost;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public RecipientAddressModel getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(RecipientAddressModel recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<CartSellerItemModel> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartSellerItemModel> cartItem) {
        this.cartItem = cartItem;
    }

    public ShipmentCostModel getShipmentCost() {
        return shipmentCost;
    }

    public void setShipmentCost(ShipmentCostModel shipmentCost) {
        this.shipmentCost = shipmentCost;
    }

    public SingleShipmentData() {
    }

    public static final class Builder {
        private RecipientAddressModel recipientAddress;
        private List<CartSellerItemModel> cartItem;
        private ShipmentCostModel shipmentCost;

        public Builder() {
        }

        public Builder recipientAddress(RecipientAddressModel val) {
            recipientAddress = val;
            return this;
        }

        public Builder cartItem(List<CartSellerItemModel> val) {
            cartItem = val;
            return this;
        }

        public Builder shipmentCost(ShipmentCostModel val) {
            shipmentCost = val;
            return this;
        }

        public SingleShipmentData build() {
            return new SingleShipmentData(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
        dest.writeByte(this.isWarning ? (byte) 1 : (byte) 0);
        dest.writeString(this.warningMessage);
        dest.writeParcelable(this.recipientAddress, flags);
        dest.writeTypedList(this.cartItem);
        dest.writeParcelable(this.shipmentCost, flags);
    }

    protected SingleShipmentData(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.isWarning = in.readByte() != 0;
        this.warningMessage = in.readString();
        this.recipientAddress = in.readParcelable(RecipientAddressModel.class.getClassLoader());
        this.cartItem = in.createTypedArrayList(CartSellerItemModel.CREATOR);
        this.shipmentCost = in.readParcelable(ShipmentCostModel.class.getClassLoader());
    }

    public static final Creator<SingleShipmentData> CREATOR = new Creator<SingleShipmentData>() {
        @Override
        public SingleShipmentData createFromParcel(Parcel source) {
            return new SingleShipmentData(source);
        }

        @Override
        public SingleShipmentData[] newArray(int size) {
            return new SingleShipmentData[size];
        }
    };
}
