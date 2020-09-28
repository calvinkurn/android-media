package com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class CourierUiModel implements Parcelable {

    private String courierId;

    private String courierImageUrl;

    private String courierName;

    private boolean selected;

    private List<CourierServiceModel> courierServiceList;

    public CourierUiModel() {
    }

    protected CourierUiModel(Parcel in) {
        courierId = in.readString();
        courierImageUrl = in.readString();
        courierName = in.readString();
        selected = in.readByte() != 0;
        courierServiceList = in.createTypedArrayList(CourierServiceModel.CREATOR);
    }

    public static final Creator<CourierUiModel> CREATOR = new Creator<CourierUiModel>() {
        @Override
        public CourierUiModel createFromParcel(Parcel in) {
            return new CourierUiModel(in);
        }

        @Override
        public CourierUiModel[] newArray(int size) {
            return new CourierUiModel[size];
        }
    };

    public String getCourierImageUrl() {
        return courierImageUrl;
    }

    public void setCourierImageUrl(String courierImageUrl) {
        this.courierImageUrl = courierImageUrl;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    public List<CourierServiceModel> getCourierServiceList() {
        return courierServiceList;
    }

    public void setCourierServiceList(List<CourierServiceModel> courierServiceList) {
        this.courierServiceList = courierServiceList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(courierId);
        parcel.writeString(courierImageUrl);
        parcel.writeString(courierName);
        parcel.writeByte((byte) (selected ? 1 : 0));
        parcel.writeTypedList(courierServiceList);
    }
}
