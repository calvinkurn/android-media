//package com.tokopedia.core.payment.model.responsecalculateshipping;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author anggaprasetiyo on 11/18/16.
// */
//
//public class Shipment implements Parcelable {
//
//    @SerializedName("shipping_max_add_fee")
//    @Expose
//    private Integer shippingMaxAddFee;
//    @SerializedName("shipment_id")
//    @Expose
//    private String shipmentId;
//    @SerializedName("shipment_package")
//    @Expose
//    private List<ShipmentPackage> shipmentPackage = new ArrayList<ShipmentPackage>();
//    @SerializedName("shipment_available")
//    @Expose
//    private Integer shipmentAvailable;
//    @SerializedName("shipment_image")
//    @Expose
//    private String shipmentImage;
//    @SerializedName("shipment_name")
//    @Expose
//    private String shipmentName;
//
//    protected Shipment(Parcel in) {
//        shipmentId = in.readString();
//        shipmentImage = in.readString();
//        shipmentName = in.readString();
//    }
//
//    public static final Creator<Shipment> CREATOR = new Creator<Shipment>() {
//        @Override
//        public Shipment createFromParcel(Parcel in) {
//            return new Shipment(in);
//        }
//
//        @Override
//        public Shipment[] newArray(int size) {
//            return new Shipment[size];
//        }
//    };
//
//    public Shipment() {
//
//    }
//
//    /**
//     * @return The shippingMaxAddFee
//     */
//    public Integer getShippingMaxAddFee() {
//        return shippingMaxAddFee;
//    }
//
//    /**
//     * @param shippingMaxAddFee The shipping_max_add_fee
//     */
//    public void setShippingMaxAddFee(Integer shippingMaxAddFee) {
//        this.shippingMaxAddFee = shippingMaxAddFee;
//    }
//
//    /**
//     * @return The shipmentId
//     */
//    public String getShipmentId() {
//        return shipmentId;
//    }
//
//    /**
//     * @param shipmentId The shipment_id
//     */
//    public void setShipmentId(String shipmentId) {
//        this.shipmentId = shipmentId;
//    }
//
//    /**
//     * @return The shipmentPackage
//     */
//    public List<ShipmentPackage> getShipmentPackage() {
//        return shipmentPackage;
//    }
//
//    /**
//     * @param shipmentPackage The shipment_package
//     */
//    public void setShipmentPackage(List<ShipmentPackage> shipmentPackage) {
//        this.shipmentPackage = shipmentPackage;
//    }
//
//    /**
//     * @return The shipmentAvailable
//     */
//    public Integer getShipmentAvailable() {
//        return shipmentAvailable;
//    }
//
//    /**
//     * @param shipmentAvailable The shipment_available
//     */
//    public void setShipmentAvailable(Integer shipmentAvailable) {
//        this.shipmentAvailable = shipmentAvailable;
//    }
//
//    /**
//     * @return The shipmentImage
//     */
//    public String getShipmentImage() {
//        return shipmentImage;
//    }
//
//    /**
//     * @param shipmentImage The shipment_image
//     */
//    public void setShipmentImage(String shipmentImage) {
//        this.shipmentImage = shipmentImage;
//    }
//
//    /**
//     * @return The shipmentName
//     */
//    public String getShipmentName() {
//        return shipmentName;
//    }
//
//    /**
//     * @param shipmentName The shipment_name
//     */
//    public void setShipmentName(String shipmentName) {
//        this.shipmentName = shipmentName;
//    }
//
//    @Override
//    public String toString() {
//        return shipmentName;
//    }
//
//    public static Shipment createSelectionInfo(String info) {
//        Shipment shipment = new Shipment();
//        shipment.setShipmentName(info);
//        shipment.setShipmentId("0");
//        shipment.setShipmentPackage(new ArrayList<ShipmentPackage>());
//        return shipment;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(shipmentId);
//        dest.writeString(shipmentImage);
//        dest.writeString(shipmentName);
//    }
//}
