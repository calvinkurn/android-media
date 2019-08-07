package com.tokopedia.checkout.view.feature.shipment;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;

/**
 * @author anggaprasetiyo on 03/07/18.
 */
public class ShipmentSelectionStateData implements Parcelable {
    private CourierItemData courierItemData;
    private int position;

    public CourierItemData getCourierItemData() {
        return courierItemData;
    }

    public void setCourierItemData(CourierItemData courierItemData) {
        this.courierItemData = courierItemData;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.courierItemData, flags);
        dest.writeInt(this.position);
    }

    public ShipmentSelectionStateData() {
    }

    @Override
    public int hashCode() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof ShipmentSelectionStateData)) return false;
        ShipmentSelectionStateData other = (ShipmentSelectionStateData) obj;
        return this.position == other.position;
    }

    protected ShipmentSelectionStateData(Parcel in) {
        this.courierItemData = in.readParcelable(CourierItemData.class.getClassLoader());
        this.position = in.readInt();
    }

    public static final Parcelable.Creator<ShipmentSelectionStateData> CREATOR
            = new Parcelable.Creator<ShipmentSelectionStateData>() {
        @Override
        public ShipmentSelectionStateData createFromParcel(Parcel source) {
            return new ShipmentSelectionStateData(source);
        }

        @Override
        public ShipmentSelectionStateData[] newArray(int size) {
            return new ShipmentSelectionStateData[size];
        }
    };
}
