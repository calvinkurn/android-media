package com.tokopedia.flight.search.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;

/**
 * Created by alvarisi on 11/1/17.
 */

public class FlightSearchPassDataViewModel implements Parcelable{
    private String departureDate;
    private String returnDate;
    private boolean isOneWay;
    private FlightPassengerViewModel flightPassengerViewModel;
    private FlightAirportViewModel departureAirport;
    private FlightAirportViewModel arrivalAirport;
    private FlightClassViewModel flightClass;

    public FlightSearchPassDataViewModel(String departureDate, String returnDate, boolean isOneWay, FlightPassengerViewModel flightPassengerViewModel, FlightAirportViewModel departureAirport, FlightAirportViewModel arrivalAirport, FlightClassViewModel flightClass) {
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.isOneWay = isOneWay;
        this.flightPassengerViewModel = flightPassengerViewModel;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.flightClass = flightClass;
    }

    public FlightSearchPassDataViewModel() {
    }

    protected FlightSearchPassDataViewModel(Parcel in) {
        departureDate = in.readString();
        returnDate = in.readString();
        isOneWay = in.readByte() != 0;
        flightPassengerViewModel = in.readParcelable(FlightPassengerViewModel.class.getClassLoader());
        departureAirport = in.readParcelable(FlightAirportViewModel.class.getClassLoader());
        arrivalAirport = in.readParcelable(FlightAirportViewModel.class.getClassLoader());
        flightClass = in.readParcelable(FlightClassViewModel.class.getClassLoader());
    }

    public static final Creator<FlightSearchPassDataViewModel> CREATOR = new Creator<FlightSearchPassDataViewModel>() {
        @Override
        public FlightSearchPassDataViewModel createFromParcel(Parcel in) {
            return new FlightSearchPassDataViewModel(in);
        }

        @Override
        public FlightSearchPassDataViewModel[] newArray(int size) {
            return new FlightSearchPassDataViewModel[size];
        }
    };

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getDate(boolean isReturning) {
        return isReturning ? getReturnDate() : getDepartureDate();
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public void setOneWay(boolean oneWay) {
        isOneWay = oneWay;
    }

    public FlightPassengerViewModel getFlightPassengerViewModel() {
        return flightPassengerViewModel;
    }

    public void setFlightPassengerViewModel(FlightPassengerViewModel flightPassengerViewModel) {
        this.flightPassengerViewModel = flightPassengerViewModel;
    }

    public FlightAirportViewModel getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(FlightAirportViewModel departureAirport) {
        this.departureAirport = departureAirport;
    }

    public FlightAirportViewModel getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(FlightAirportViewModel arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public FlightClassViewModel getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(FlightClassViewModel flightClass) {
        this.flightClass = flightClass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(departureDate);
        parcel.writeString(returnDate);
        parcel.writeByte((byte) (isOneWay ? 1 : 0));
        parcel.writeParcelable(flightPassengerViewModel, i);
        parcel.writeParcelable(departureAirport, i);
        parcel.writeParcelable(arrivalAirport, i);
        parcel.writeParcelable(flightClass, i);
    }

    public static class Builder {
        private String departureDate;
        private String returnDate;
        private boolean isOneWay;
        private FlightPassengerViewModel flightPassengerViewModel;
        private FlightAirportViewModel departureAirport;
        private FlightAirportViewModel arrivalAirport;
        private FlightClassViewModel flightClass;

        public Builder() {
        }

        public Builder setDepartureDate(String departureDate) {
            this.departureDate = departureDate;
            return this;
        }

        public Builder setReturnDate(String returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public Builder setIsOneWay(boolean isOneWay) {
            this.isOneWay = isOneWay;
            return this;
        }

        public Builder setFlightPassengerViewModel(FlightPassengerViewModel flightPassengerViewModel) {
            this.flightPassengerViewModel = flightPassengerViewModel;
            return this;
        }

        public Builder setDepartureAirport(FlightAirportViewModel departureAirport) {
            this.departureAirport = departureAirport;
            return this;
        }

        public Builder setArrivalAirport(FlightAirportViewModel arrivalAirport) {
            this.arrivalAirport = arrivalAirport;
            return this;
        }

        public Builder setFlightClass(FlightClassViewModel flightClass) {
            this.flightClass = flightClass;
            return this;
        }

        public FlightSearchPassDataViewModel build() {
            return new FlightSearchPassDataViewModel(departureDate, returnDate, isOneWay, flightPassengerViewModel, departureAirport, arrivalAirport, flightClass);
        }
    }
}
