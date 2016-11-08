package com.tokopedia.core.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * CartShipments
 * Created by Angga.Prasetiyo on 05/07/2016.
 */
public class CartShipments implements Parcelable{
    @SerializedName("shipment_package_name")
    @Expose
    private String shipmentPackageName;
    @SerializedName("shipment_package_id")
    @Expose
    private String shipmentPackageId;
    @SerializedName("shipment_id")
    @Expose
    private String shipmentId;
    @SerializedName("shipment_notes")
    @Expose
    private String shipmentNotes;
    @SerializedName("shipment_image")
    @Expose
    private String shipmentImage;
    @SerializedName("shipment_name")
    @Expose
    private String shipmentName;

    public String getShipmentPackageName() {
        return shipmentPackageName;
    }

    public void setShipmentPackageName(String shipmentPackageName) {
        this.shipmentPackageName = shipmentPackageName;
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

    public String getShipmentNotes() {
        return shipmentNotes;
    }

    public void setShipmentNotes(String shipmentNotes) {
        this.shipmentNotes = shipmentNotes;
    }

    public String getShipmentImage() {
        return shipmentImage;
    }

    public void setShipmentImage(String shipmentImage) {
        this.shipmentImage = shipmentImage;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    protected CartShipments(Parcel in) {
        shipmentPackageName = in.readString();
        shipmentPackageId = in.readString();
        shipmentId = in.readString();
        shipmentNotes = in.readString();
        shipmentImage = in.readString();
        shipmentName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shipmentPackageName);
        dest.writeString(shipmentPackageId);
        dest.writeString(shipmentId);
        dest.writeString(shipmentNotes);
        dest.writeString(shipmentImage);
        dest.writeString(shipmentName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CartShipments> CREATOR = new Parcelable.Creator<CartShipments>() {
        @Override
        public CartShipments createFromParcel(Parcel in) {
            return new CartShipments(in);
        }

        @Override
        public CartShipments[] newArray(int size) {
            return new CartShipments[size];
        }
    };
}
