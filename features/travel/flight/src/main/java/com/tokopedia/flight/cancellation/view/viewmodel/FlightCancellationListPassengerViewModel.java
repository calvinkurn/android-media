package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationDetailPassengerAdapterTypeFactory;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderAmenityViewModel;

import java.util.List;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationListPassengerViewModel extends FlightCancellationPassengerViewModel
        implements Visitable<FlightCancellationDetailPassengerAdapterTypeFactory> {

    private List<FlightOrderAmenityViewModel> amenities;
    private long journeyId;
    private String arrivalAirportId;
    private String departureAiportId;

    public FlightCancellationListPassengerViewModel() {
    }

    public List<FlightOrderAmenityViewModel> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<FlightOrderAmenityViewModel> amenities) {
        this.amenities = amenities;
    }

    public FlightCancellationListPassengerViewModel(Parcel in) {
        super(in);
        amenities = in.createTypedArrayList(FlightOrderAmenityViewModel.CREATOR);
        journeyId = in.readLong();
        arrivalAirportId = in.readString();
        departureAiportId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(amenities);
        dest.writeLong(journeyId);
        dest.writeString(arrivalAirportId);
        dest.writeString(departureAiportId);
    }

    public static final Creator<FlightCancellationListPassengerViewModel> CREATOR = new Creator<FlightCancellationListPassengerViewModel>() {
        @Override
        public FlightCancellationListPassengerViewModel createFromParcel(Parcel in) {
            return new FlightCancellationListPassengerViewModel(in);
        }

        @Override
        public FlightCancellationListPassengerViewModel[] newArray(int size) {
            return new FlightCancellationListPassengerViewModel[size];
        }
    };

    @Override
    public int type(FlightCancellationDetailPassengerAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(long journeyId) {
        this.journeyId = journeyId;
    }

    public String getArrivalAirportId() {
        return arrivalAirportId;
    }

    public void setArrivalAirportId(String arrivalAirportId) {
        this.arrivalAirportId = arrivalAirportId;
    }

    public String getDepartureAiportId() {
        return departureAiportId;
    }

    public void setDepartureAiportId(String departureAiportId) {
        this.departureAiportId = departureAiportId;
    }
}
