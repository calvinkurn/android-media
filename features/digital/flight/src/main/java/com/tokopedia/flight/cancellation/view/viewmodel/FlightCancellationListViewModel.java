package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationListTypeFactory;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationListViewModel implements Parcelable,
        Visitable<FlightCancellationListTypeFactory> {

    private String orderId;
    private FlightCancellationDetail cancellations;

    public FlightCancellationListViewModel() {
    }

    protected FlightCancellationListViewModel(Parcel in) {
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

    public static final Creator<FlightCancellationListViewModel> CREATOR = new Creator<FlightCancellationListViewModel>() {
        @Override
        public FlightCancellationListViewModel createFromParcel(Parcel in) {
            return new FlightCancellationListViewModel(in);
        }

        @Override
        public FlightCancellationListViewModel[] newArray(int size) {
            return new FlightCancellationListViewModel[size];
        }
    };

    @Override
    public int type(FlightCancellationListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

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
