package com.tokopedia.checkout.domain.datamodel.cartsingleshipment;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.checkout.view.view.shipment.ShipmentData;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class ShipmentCostModel implements Parcelable, ShipmentData {

    private int totalItem;
    private double totalItemPrice;
    private double totalPrice;
    private double totalWeight;
    private double shippingFee;
    private double insuranceFee;
    private double additionalFee;
    private double promoPrice;
    private String promoMessage;

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public double getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(double totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public double getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(double insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public double getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(double promoPrice) {
        this.promoPrice = promoPrice;
    }

    public String getPromoMessage() {
        return promoMessage;
    }

    public void setPromoMessage(String promoMessage) {
        this.promoMessage = promoMessage;
    }

    public double getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(double additionalFee) {
        this.additionalFee = additionalFee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.totalItem);
        dest.writeDouble(this.totalItemPrice);
        dest.writeDouble(this.totalPrice);
        dest.writeDouble(this.totalWeight);
        dest.writeDouble(this.shippingFee);
        dest.writeDouble(this.insuranceFee);
        dest.writeDouble(this.promoPrice);
        dest.writeString(this.promoMessage);
        dest.writeDouble(this.additionalFee);
    }

    public ShipmentCostModel() {
    }

    protected ShipmentCostModel(Parcel in) {
        this.totalItem = in.readInt();
        this.totalItemPrice = in.readDouble();
        this.totalPrice = in.readDouble();
        this.totalWeight = in.readDouble();
        this.shippingFee = in.readDouble();
        this.insuranceFee = in.readDouble();
        this.promoPrice = in.readDouble();
        this.promoMessage = in.readString();
        this.additionalFee = in.readDouble();
    }

    public static final Creator<ShipmentCostModel> CREATOR = new Creator<ShipmentCostModel>() {
        @Override
        public ShipmentCostModel createFromParcel(Parcel source) {
            return new ShipmentCostModel(source);
        }

        @Override
        public ShipmentCostModel[] newArray(int size) {
            return new ShipmentCostModel[size];
        }
    };

}
