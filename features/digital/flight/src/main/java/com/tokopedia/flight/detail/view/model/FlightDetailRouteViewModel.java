package com.tokopedia.flight.detail.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.detail.view.adapter.FlightDetailRouteTypeFactory;
import com.tokopedia.flight.search.data.api.single.response.Amenity;

import java.util.List;

/**
 * @author alvarisi
 */

public class FlightDetailRouteViewModel implements Parcelable, Visitable<FlightDetailRouteTypeFactory> {

    private String pnr;
    private String airlineName;
    private String airlineCode;
    private String airlineLogo;
    private String flightNumber;
    private String departureTimestamp;
    private String departureAirportCity;
    private String departureAirportCode;
    private String departureAirportName;
    private boolean isRefundable;
    private String duration;
    private String arrivalTimestamp;
    private String arrivalAirportCity;
    private String arrivalAirportCode;
    private String arrivalAirportName;
    private String layover;
    private int stopOver;
    private List<FlightDetailRouteInfoViewModel> infos;
    private List<Amenity> amenities = null;
    private List<FlightStopOverViewModel> stopOverDetail;

    public FlightDetailRouteViewModel() {
    }

    protected FlightDetailRouteViewModel(Parcel in) {
        pnr = in.readString();
        airlineName = in.readString();
        airlineCode = in.readString();
        airlineLogo = in.readString();
        flightNumber = in.readString();
        departureTimestamp = in.readString();
        departureAirportCity = in.readString();
        departureAirportCode = in.readString();
        departureAirportName = in.readString();
        isRefundable = in.readByte() != 0;
        duration = in.readString();
        arrivalTimestamp = in.readString();
        arrivalAirportCity = in.readString();
        arrivalAirportCode = in.readString();
        arrivalAirportName = in.readString();
        layover = in.readString();
        stopOver = in.readInt();
        infos = in.createTypedArrayList(FlightDetailRouteInfoViewModel.CREATOR);
        amenities = in.createTypedArrayList(Amenity.CREATOR);
        stopOverDetail = in.createTypedArrayList(FlightStopOverViewModel.CREATOR);
    }

    public static final Creator<FlightDetailRouteViewModel> CREATOR = new Creator<FlightDetailRouteViewModel>() {
        @Override
        public FlightDetailRouteViewModel createFromParcel(Parcel in) {
            return new FlightDetailRouteViewModel(in);
        }

        @Override
        public FlightDetailRouteViewModel[] newArray(int size) {
            return new FlightDetailRouteViewModel[size];
        }
    };

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getAirlineLogo() {
        return airlineLogo;
    }

    public void setAirlineLogo(String airlineLogo) {
        this.airlineLogo = airlineLogo;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public void setDepartureTimestamp(String departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }

    public String getDepartureAirportCity() {
        return departureAirportCity;
    }

    public void setDepartureAirportCity(String departureAirportCity) {
        this.departureAirportCity = departureAirportCity;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(boolean refundable) {
        isRefundable = refundable;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public void setArrivalTimestamp(String arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public String getArrivalAirportCity() {
        return arrivalAirportCity;
    }

    public void setArrivalAirportCity(String arrivalAirportCity) {
        this.arrivalAirportCity = arrivalAirportCity;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public String getLayover() {
        return layover;
    }

    public void setLayover(String layover) {
        this.layover = layover;
    }

    public List<FlightDetailRouteInfoViewModel> getInfos() {
        return infos;
    }

    public void setInfos(List<FlightDetailRouteInfoViewModel> infos) {
        this.infos = infos;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    @Override
    public int type(FlightDetailRouteTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public int getStopOver() {
        return stopOver;
    }

    public void setStopOver(int stopOver) {
        this.stopOver = stopOver;
    }

    public List<FlightStopOverViewModel> getStopOverDetail() {
        return stopOverDetail;
    }

    public void setStopOverDetail(List<FlightStopOverViewModel> stopOverDetail) {
        this.stopOverDetail = stopOverDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pnr);
        parcel.writeString(airlineName);
        parcel.writeString(airlineCode);
        parcel.writeString(airlineLogo);
        parcel.writeString(flightNumber);
        parcel.writeString(departureTimestamp);
        parcel.writeString(departureAirportCity);
        parcel.writeString(departureAirportCode);
        parcel.writeString(departureAirportName);
        parcel.writeByte((byte) (isRefundable ? 1 : 0));
        parcel.writeString(duration);
        parcel.writeString(arrivalTimestamp);
        parcel.writeString(arrivalAirportCity);
        parcel.writeString(arrivalAirportCode);
        parcel.writeString(arrivalAirportName);
        parcel.writeString(layover);
        parcel.writeInt(stopOver);
        parcel.writeTypedList(infos);
        parcel.writeTypedList(amenities);
        parcel.writeTypedList(stopOverDetail);
    }
}
