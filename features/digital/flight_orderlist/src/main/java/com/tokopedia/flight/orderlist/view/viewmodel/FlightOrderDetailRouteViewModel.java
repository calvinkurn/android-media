package com.tokopedia.flight.orderlist.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.orderlist.data.cloud.entity.Amenity;

import java.util.List;

/**
 * @author alvarisi
 */

public class FlightOrderDetailRouteViewModel implements Parcelable {

    private String pnr;
    private String airlineName;
    private String airlineCode;
    private String airlineLogo;
    private String flightNumber;
    private String departureTimestamp;
    private String departureAirportCity;
    private String departureAirportCode;
    private String departureAirportName;
    private String departureTerminal;
    private boolean isRefundable;
    private String duration;
    private String arrivalTimestamp;
    private String arrivalAirportCity;
    private String arrivalAirportCode;
    private String arrivalAirportName;
    private String arrivalTerminal;
    private String layover;
    private int stopOver;
    private List<FlightOrderDetailRouteInfoViewModel> infos;
    private List<Amenity> amenities = null;
    private List<FlightStopOverViewModel> stopOverDetail;

    public FlightOrderDetailRouteViewModel() {
    }

    protected FlightOrderDetailRouteViewModel(Parcel in) {
        pnr = in.readString();
        airlineName = in.readString();
        airlineCode = in.readString();
        airlineLogo = in.readString();
        flightNumber = in.readString();
        departureTimestamp = in.readString();
        departureAirportCity = in.readString();
        departureAirportCode = in.readString();
        departureAirportName = in.readString();
        departureTerminal = in.readString();
        isRefundable = in.readByte() != 0;
        duration = in.readString();
        arrivalTimestamp = in.readString();
        arrivalAirportCity = in.readString();
        arrivalAirportCode = in.readString();
        arrivalAirportName = in.readString();
        arrivalTerminal = in.readString();
        layover = in.readString();
        stopOver = in.readInt();
        infos = in.createTypedArrayList(FlightOrderDetailRouteInfoViewModel.CREATOR);
        amenities = in.createTypedArrayList(Amenity.CREATOR);
        stopOverDetail = in.createTypedArrayList(FlightStopOverViewModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pnr);
        dest.writeString(airlineName);
        dest.writeString(airlineCode);
        dest.writeString(airlineLogo);
        dest.writeString(flightNumber);
        dest.writeString(departureTimestamp);
        dest.writeString(departureAirportCity);
        dest.writeString(departureAirportCode);
        dest.writeString(departureAirportName);
        dest.writeString(departureTerminal);
        dest.writeByte((byte) (isRefundable ? 1 : 0));
        dest.writeString(duration);
        dest.writeString(arrivalTimestamp);
        dest.writeString(arrivalAirportCity);
        dest.writeString(arrivalAirportCode);
        dest.writeString(arrivalAirportName);
        dest.writeString(arrivalTerminal);
        dest.writeString(layover);
        dest.writeInt(stopOver);
        dest.writeTypedList(infos);
        dest.writeTypedList(amenities);
        dest.writeTypedList(stopOverDetail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightOrderDetailRouteViewModel> CREATOR = new Creator<FlightOrderDetailRouteViewModel>() {
        @Override
        public FlightOrderDetailRouteViewModel createFromParcel(Parcel in) {
            return new FlightOrderDetailRouteViewModel(in);
        }

        @Override
        public FlightOrderDetailRouteViewModel[] newArray(int size) {
            return new FlightOrderDetailRouteViewModel[size];
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

    public List<FlightOrderDetailRouteInfoViewModel> getInfos() {
        return infos;
    }

    public void setInfos(List<FlightOrderDetailRouteInfoViewModel> infos) {
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

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }
}
