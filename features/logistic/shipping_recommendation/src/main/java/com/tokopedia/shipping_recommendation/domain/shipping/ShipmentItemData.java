package com.tokopedia.shipping_recommendation.domain.shipping;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Irfan Khoirul on 25/01/18.
 */

public class ShipmentItemData implements Parcelable {
    private int serviceId;
    private String type;
    private String singlePriceRange;
    private String multiplePriceRange;
    private String deliveryTimeRange;
    private boolean lessThanADayDelivery;
    private List<CourierItemData> courierItemData;
    private boolean selected;

    public ShipmentItemData() {
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int id) {
        this.serviceId = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSinglePriceRange() {
        return singlePriceRange;
    }

    public void setSinglePriceRange(String singlePriceRange) {
        this.singlePriceRange = singlePriceRange;
    }

    public String getDeliveryTimeRange() {
        return deliveryTimeRange;
    }

    public void setDeliveryTimeRange(String deliveryTimeRange) {
        this.deliveryTimeRange = deliveryTimeRange;
    }

    public List<CourierItemData> getCourierItemData() {
        return courierItemData;
    }

    public void setCourierItemData(List<CourierItemData> courierItemData) {
        this.courierItemData = courierItemData;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getMultiplePriceRange() {
        return multiplePriceRange;
    }

    public void setMultiplePriceRange(String multiplePriceRange) {
        this.multiplePriceRange = multiplePriceRange;
    }

    public boolean isLessThanADayDelivery() {
        return lessThanADayDelivery;
    }

    public void setLessThanADayDelivery(boolean lessThanADayDelivery) {
        this.lessThanADayDelivery = lessThanADayDelivery;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.serviceId);
        dest.writeString(this.type);
        dest.writeString(this.singlePriceRange);
        dest.writeString(this.multiplePriceRange);
        dest.writeString(this.deliveryTimeRange);
        dest.writeByte(this.lessThanADayDelivery ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.courierItemData);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected ShipmentItemData(Parcel in) {
        this.serviceId = in.readInt();
        this.type = in.readString();
        this.singlePriceRange = in.readString();
        this.multiplePriceRange = in.readString();
        this.deliveryTimeRange = in.readString();
        this.lessThanADayDelivery = in.readByte() != 0;
        this.courierItemData = in.createTypedArrayList(CourierItemData.CREATOR);
        this.selected = in.readByte() != 0;
    }

    public static final Creator<ShipmentItemData> CREATOR = new Creator<ShipmentItemData>() {
        @Override
        public ShipmentItemData createFromParcel(Parcel source) {
            return new ShipmentItemData(source);
        }

        @Override
        public ShipmentItemData[] newArray(int size) {
            return new ShipmentItemData[size];
        }
    };
}
