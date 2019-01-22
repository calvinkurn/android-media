package com.tokopedia.flight.search.data.api.single.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 10/26/2017.
 */

public class Route implements Parcelable {

    @SerializedName("airline")
    @Expose
    private String airline;
    @SerializedName("departure_airport")
    @Expose
    private String departureAirport;
    @SerializedName("departure_timestamp")
    @Expose
    private String departureTimestamp;
    @SerializedName("arrival_airport")
    @Expose
    private String arrivalAirport;
    @SerializedName("arrival_timestamp")
    @Expose
    private String arrivalTimestamp;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("layover")
    @Expose
    private String layover;
    @SerializedName("infos")
    @Expose
    private List<Info> infos = null;
    @SerializedName("flight_number")
    @Expose
    private String flightNumber;
    @SerializedName("is_refundable")
    @Expose
    private boolean isRefundable;
    @SerializedName("amenities")
    @Expose
    private List<Amenity> amenities = null;
    @SerializedName("stop")
    @Expose
    private int stops;
    @SerializedName("stop_detail")
    @Expose
    private List<StopDetailEntity> stopDetails;

    private String airlineName; // mergeResult
    private String airlineLogo; // mergeResult

    private String departureAirportName; // mergeResult
    private String departureAirportCity; // mergeResult

    private String arrivalAirportName; // mergeResult
    private String arrivalAirportCity; // mergeResult

    public Route(String airline, String departureAirport, String departureTimestamp, String arrivalAirport,
                 String arrivalTimestamp, String duration, String layover, List<Info> infos, String flightNumber,
                 boolean isRefundable, List<Amenity> amenities, int stops, List<StopDetailEntity> stopDetails,
                 String airlineName, String airlineLogo, String departureAirportName, String departureAirportCity,
                 String arrivalAirportName, String arrivalAirportCity) {
        this.airline = airline;
        this.departureAirport = departureAirport;
        this.departureTimestamp = departureTimestamp;
        this.arrivalAirport = arrivalAirport;
        this.arrivalTimestamp = arrivalTimestamp;
        this.duration = duration;
        this.layover = layover;
        this.infos = infos;
        this.flightNumber = flightNumber;
        this.isRefundable = isRefundable;
        this.amenities = amenities;
        this.stops = stops;
        this.stopDetails = stopDetails;
        this.airlineName = airlineName;
        this.airlineLogo = airlineLogo;
        this.departureAirportName = departureAirportName;
        this.departureAirportCity = departureAirportCity;
        this.arrivalAirportName = arrivalAirportName;
        this.arrivalAirportCity = arrivalAirportCity;
    }

    protected Route(Parcel in) {
        airline = in.readString();
        departureAirport = in.readString();
        departureTimestamp = in.readString();
        arrivalAirport = in.readString();
        arrivalTimestamp = in.readString();
        duration = in.readString();
        layover = in.readString();
        infos = in.createTypedArrayList(Info.CREATOR);
        flightNumber = in.readString();
        isRefundable = in.readByte() != 0;
        amenities = in.createTypedArrayList(Amenity.CREATOR);
        stops = in.readInt();
        stopDetails = in.createTypedArrayList(StopDetailEntity.CREATOR);
        airlineName = in.readString();
        airlineLogo = in.readString();
        departureAirportName = in.readString();
        departureAirportCity = in.readString();
        arrivalAirportName = in.readString();
        arrivalAirportCity = in.readString();
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    public String getAirline() {
        return airline;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public String getDuration() {
        return duration;
    }

    public String getLayover() {
        return layover;
    }

    public List<Info> getInfos() {
        return infos;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Boolean getRefundable() {
        return isRefundable;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public void setArrivalAirportCity(String arrivalAirportCity) {
        this.arrivalAirportCity = arrivalAirportCity;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public void setDepartureAirportCity(String departureAirportCity) {
        this.departureAirportCity = departureAirportCity;
    }

    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public String getAirlineLogo() {
        return airlineLogo;
    }

    public void setAirlineLogo(String airlineLogo) {
        this.airlineLogo = airlineLogo;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public String getDepartureAirportCity() {
        return departureAirportCity;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public String getArrivalAirportCity() {
        return arrivalAirportCity;
    }

    public int getStops() {
        return stops;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

    public List<StopDetailEntity> getStopDetails() {
        return stopDetails;
    }

    public void setStopDetails(List<StopDetailEntity> stopDetails) {
        this.stopDetails = stopDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(airline);
        parcel.writeString(departureAirport);
        parcel.writeString(departureTimestamp);
        parcel.writeString(arrivalAirport);
        parcel.writeString(arrivalTimestamp);
        parcel.writeString(duration);
        parcel.writeString(layover);
        parcel.writeTypedList(infos);
        parcel.writeString(flightNumber);
        parcel.writeByte((byte) (isRefundable ? 1 : 0));
        parcel.writeTypedList(amenities);
        parcel.writeInt(stops);
        parcel.writeTypedList(stopDetails);
        parcel.writeString(airlineName);
        parcel.writeString(airlineLogo);
        parcel.writeString(departureAirportName);
        parcel.writeString(departureAirportCity);
        parcel.writeString(arrivalAirportName);
        parcel.writeString(arrivalAirportCity);
    }
}
