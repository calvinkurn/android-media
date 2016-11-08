package com.tokopedia.core.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class OrderShipment implements Parcelable {
    private static final String TAG = OrderShipment.class.getSimpleName();

    @SerializedName("shipment_logo")
    @Expose
    private String shipmentLogo;
    @SerializedName("shipment_package_id")
    @Expose
    private String shipmentPackageId;
    @SerializedName("shipment_id")
    @Expose
    private String shipmentId;
    @SerializedName("shipment_product")
    @Expose
    private String shipmentProduct;
    @SerializedName("shipment_name")
    @Expose
    private String shipmentName;

    protected OrderShipment(Parcel in) {
        shipmentLogo = in.readString();
        shipmentPackageId = in.readString();
        shipmentId = in.readString();
        shipmentProduct = in.readString();
        shipmentName = in.readString();
    }

    public static final Creator<OrderShipment> CREATOR = new Creator<OrderShipment>() {
        @Override
        public OrderShipment createFromParcel(Parcel in) {
            return new OrderShipment(in);
        }

        @Override
        public OrderShipment[] newArray(int size) {
            return new OrderShipment[size];
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
    }
}
