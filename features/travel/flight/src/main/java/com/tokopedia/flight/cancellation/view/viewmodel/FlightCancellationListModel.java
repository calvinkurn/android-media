package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationListModel implements Parcelable{

    private String orderId;
    private FlightCancellationDetail cancellations;

    public FlightCancellationListModel() {
    }

    protected FlightCancellationListModel(Parcel in) {
        orderId = in.readString();
        cancellations = in.readParcelable(FlightCancellationDetail.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeParcelable(cancellations, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightCancellationListModel> CREATOR = new Creator<FlightCancellationListModel>() {
        @Override
        public FlightCancellationListModel createFromParcel(Parcel in) {
            return new FlightCancellationListModel(in);
        }

        @Override
        public FlightCancellationListModel[] newArray(int size) {
            return new FlightCancellationListModel[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public FlightCancellationDetail getCancellations() {
        return cancellations;
    }

    public void setCancellations(FlightCancellationDetail cancellations) {
        this.cancellations = cancellations;
    }
}
