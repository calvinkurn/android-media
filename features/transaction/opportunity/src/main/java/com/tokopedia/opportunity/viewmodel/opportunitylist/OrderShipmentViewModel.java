package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/7/17.
 */
public class OrderShipmentViewModel implements Parcelable{

    private String shipmentLogo;
    private String shipmentPackageId;
    private String shipmentId;
    private String shipmentProduct;
    private String shipmentName;
    private int sameDay;

    public OrderShipmentViewModel() {
    }

    protected OrderShipmentViewModel(Parcel in) {
        shipmentLogo = in.readString();
        shipmentPackageId = in.readString();
        shipmentId = in.readString();
        shipmentProduct = in.readString();
        shipmentName = in.readString();
        sameDay = in.readInt();
    }

    public static final Creator<OrderShipmentViewModel> CREATOR = new Creator<OrderShipmentViewModel>() {
        @Override
        public OrderShipmentViewModel createFromParcel(Parcel in) {
            return new OrderShipmentViewModel(in);
        }

        @Override
        public OrderShipmentViewModel[] newArray(int size) {
            return new OrderShipmentViewModel[size];
        }
    };

    public String getShipmentLogo() {
        return shipmentLogo;
    }

    public void setShipmentLogo(String shipmentLogo) {
        this.shipmentLogo = shipmentLogo;
    }

    public String getShipmentPackageId() {
        return shipmentPackageId;
    }

    public void setShipmentPackageId(String shipmentPackageId) {
        this.shipmentPackageId = shipmentPackageId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentProduct() {
        return shipmentProduct;
    }

    public void setShipmentProduct(String shipmentProduct) {
        this.shipmentProduct = shipmentProduct;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public int getSameDay() {
        return sameDay;
    }

    public void setSameDay(int sameDay) {
        this.sameDay = sameDay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shipmentLogo);
        dest.writeString(shipmentPackageId);
        dest.writeString(shipmentId);
        dest.writeString(shipmentProduct);
        dest.writeString(shipmentName);
        dest.writeInt(sameDay);
    }
}
