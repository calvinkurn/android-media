package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationTypeFactory;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney;

import java.util.List;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationModel implements Parcelable,
        Visitable<FlightCancellationTypeFactory> {

    private String invoiceId;
    private FlightCancellationJourney flightCancellationJourney;
    private List<FlightCancellationPassengerModel> passengerViewModelList;

    public FlightCancellationModel() {
    }

    protected FlightCancellationModel(Parcel in) {
        invoiceId = in.readString();
        flightCancellationJourney = in.readParcelable(FlightCancellationJourney.class.getClassLoader());
        passengerViewModelList = in.createTypedArrayList(FlightCancellationPassengerModel.CREATOR);
    }

    public static final Creator<FlightCancellationModel> CREATOR = new Creator<FlightCancellationModel>() {
        @Override
        public FlightCancellationModel createFromParcel(Parcel in) {
            return new FlightCancellationModel(in);
        }

        @Override
        public FlightCancellationModel[] newArray(int size) {
            return new FlightCancellationModel[size];
        }
    };

    @Override
    public int type(FlightCancellationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public FlightCancellationJourney getFlightCancellationJourney() {
        return flightCancellationJourney;
    }

    public void setFlightCancellationJourney(FlightCancellationJourney flightCancellationJourney) {
        this.flightCancellationJourney = flightCancellationJourney;
    }

    public List<FlightCancellationPassengerModel> getPassengerViewModelList() {
        return passengerViewModelList;
    }

    public void setPassengerViewModelList(List<FlightCancellationPassengerModel> passengerViewModelList) {
        this.passengerViewModelList = passengerViewModelList;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(invoiceId);
        parcel.writeParcelable(flightCancellationJourney, i);
        parcel.writeTypedList(passengerViewModelList);
    }

}
