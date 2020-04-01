package com.tokopedia.flight.orderlist.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 12/8/17.
 */

public class FlightOrderDetailRouteInfoViewModel implements Parcelable {
    public static final Creator<FlightOrderDetailRouteInfoViewModel> CREATOR = new Creator<FlightOrderDetailRouteInfoViewModel>() {
        @Override
        public FlightOrderDetailRouteInfoViewModel createFromParcel(Parcel in) {
            return new FlightOrderDetailRouteInfoViewModel(in);
        }

        @Override
        public FlightOrderDetailRouteInfoViewModel[] newArray(int size) {
            return new FlightOrderDetailRouteInfoViewModel[size];
        }
    };
    private String label;
    private String value;

    public FlightOrderDetailRouteInfoViewModel() {
    }

    protected FlightOrderDetailRouteInfoViewModel(Parcel in) {
        label = in.readString();
        value = in.readString();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(label);
        parcel.writeString(value);
    }
}
