package com.tokopedia.flight.search.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.airport.view.model.FlightAirportModel;
import com.tokopedia.flight.dashboard.view.fragment.model.FlightClassModel;
import com.tokopedia.flight.dashboard.view.fragment.model.FlightPassengerModel;

/**
 * Created by alvarisi on 11/1/17.
 */

public class FlightSearchPassDataModel implements Parcelable{
    private String departureDate;
    private String returnDate;
    private boolean isOneWay;
    private FlightPassengerModel flightPassengerViewModel;
    private FlightAirportModel departureAirport;
    private FlightAirportModel arrivalAirport;
    private FlightClassModel flightClass;
    private String linkUrl;
    private String searchRequestId;

    public FlightSearchPassDataModel(String departureDate, String returnDate, boolean isOneWay,
                                         FlightPassengerModel flightPassengerViewModel,
                                         FlightAirportModel departureAirport, FlightAirportModel arrivalAirport,
                                         FlightClassModel flightClass, String linkUrl, String searchRequestId) {
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.isOneWay = isOneWay;
        this.flightPassengerViewModel = flightPassengerViewModel;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.flightClass = flightClass;
        this.linkUrl = linkUrl;
        this.searchRequestId = searchRequestId;
    }

    public FlightSearchPassDataModel() {
    }

    protected FlightSearchPassDataModel(Parcel in) {
        departureDate = in.readString();
        returnDate = in.readString();
        isOneWay = in.readByte() != 0;
        flightPassengerViewModel = in.readParcelable(FlightPassengerModel.class.getClassLoader());
        departureAirport = in.readParcelable(FlightAirportModel.class.getClassLoader());
        arrivalAirport = in.readParcelable(FlightAirportModel.class.getClassLoader());
        flightClass = in.readParcelable(FlightClassModel.class.getClassLoader());
        linkUrl = in.readString();
        searchRequestId = in.readString();
    }

    public static final Creator<FlightSearchPassDataModel> CREATOR = new Creator<FlightSearchPassDataModel>() {
        @Override
        public FlightSearchPassDataModel createFromParcel(Parcel in) {
            return new FlightSearchPassDataModel(in);
        }

        @Override
        public FlightSearchPassDataModel[] newArray(int size) {
            return new FlightSearchPassDataModel[size];
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

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
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

    public FlightPassengerModel getFlightPassengerViewModel() {
        return flightPassengerViewModel;
    }

    public void setFlightPassengerViewModel(FlightPassengerModel flightPassengerViewModel) {
        this.flightPassengerViewModel = flightPassengerViewModel;
    }

    public FlightAirportModel getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(FlightAirportModel departureAirport) {
        this.departureAirport = departureAirport;
    }

    public FlightAirportModel getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(FlightAirportModel arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public FlightClassModel getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(FlightClassModel flightClass) {
        this.flightClass = flightClass;
    }

    public String getSearchRequestId() {
        return searchRequestId;
    }

    public void setSearchRequestId(String searchRequestId) {
        this.searchRequestId = searchRequestId;
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
        parcel.writeString(linkUrl);
        parcel.writeString(searchRequestId);
    }

    public static class Builder {
        private String departureDate;
        private String returnDate;
        private boolean isOneWay;
        private FlightPassengerModel flightPassengerViewModel;
        private FlightAirportModel departureAirport;
        private FlightAirportModel arrivalAirport;
        private FlightClassModel flightClass;
        private String linkUrl;
        private String searchRequestId;

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

        public Builder setFlightPassengerViewModel(FlightPassengerModel flightPassengerViewModel) {
            this.flightPassengerViewModel = flightPassengerViewModel;
            return this;
        }

        public Builder setDepartureAirport(FlightAirportModel departureAirport) {
            this.departureAirport = departureAirport;
            return this;
        }

        public Builder setArrivalAirport(FlightAirportModel arrivalAirport) {
            this.arrivalAirport = arrivalAirport;
            return this;
        }

        public Builder setFlightClass(FlightClassModel flightClass) {
            this.flightClass = flightClass;
            return this;
        }

        public Builder setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
            return this;
        }

        public Builder setSearchRequestId(String requestId) {
            this.searchRequestId = requestId;
            return this;
        }

        public FlightSearchPassDataModel build() {
            return new FlightSearchPassDataModel(departureDate, returnDate, isOneWay,
                    flightPassengerViewModel, departureAirport, arrivalAirport, flightClass,
                    linkUrl, searchRequestId);
        }
    }
}
