package com.tokopedia.flight_dbflow;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Rizky on 25/10/18.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightSearchSingleRouteDB extends BaseModel implements Parcelable {

    public static final String ID = "id";
    public static final String TOTAL_NUMERIC = "total_numeric";
    public static final String ADULT_NUMERIC = "adult_numeric";
    public static final String AIRLINE = "airline";
    public static final String IS_REFUNDABLE = "is_refundable";
    public static final String DEPARTURE_TIME = "departure_time";
    public static final String DEPARTURE_TIME_INT = "departure_time_int";
    public static final String DURATION_MINUTE = "duration_minute";
    public static final String TOTAL_TRANSIT = "total_transit";
    public static final String BEFORE_TOTAL = "before_total";

    @PrimaryKey
    @Column(name = ID)
    private String id;

    @Column(name = "type")
    private String flightType;

    @Column(name = "term")
    private String term;

    @Column(name = "aid")
    private String aid;

    @Column(name = "departure_airport")
    private String departureAirport;

    @Column(name = DEPARTURE_TIME)
    private String departureTime;

    @Column(name = DEPARTURE_TIME_INT)
    private int departureTimeInt;

    @Column(name = "arrival_airport")
    private String arrivalAirport;

    @Column(name = "arrival_time")
    private String arrivalTime;

    @Column(name = "arrival_time_int")
    private int arrivalTimeInt;

    @Column(name = TOTAL_TRANSIT)
    private int totalTransit;

    @Column(name = "add_day_arrival")
    private int addDayArrival;

    @Column(name = "duration")
    private String duration;

    @Column(name = DURATION_MINUTE)
    private int durationMinute;

    @Column(name = "total")
    private String total;

    @Column(name = TOTAL_NUMERIC)
    private int totalNumeric;

    @Column(name = BEFORE_TOTAL)
    private String beforeTotal;

    @Column(name = "routes")
    private String routes;

    @Column(name = "adult")
    private String adult;

    @Column(name = ADULT_NUMERIC)
    private int adultNumeric;

    @Column(name = "child")
    private String child;

    @Column(name = "child_numeric")
    private int childNumeric;

    @Column(name = "infant")
    private String infant;

    @Column(name = "infant_numeric")
    private int infantNumeric;

    @Column(name = AIRLINE)
    private String airline;

    @Column(name = IS_REFUNDABLE)
    private int isRefundable;

    public FlightSearchSingleRouteDB(String id, String flightType, String term, String aid,
                                     String departureAirport, String departureTime, int departureTimeInt,
                                     String arrivalAirport, String arrivalTime, int arrivalTimeInt,
                                     int totalTransit, int addDayArrival, String duration, int durationMinute,
                                     String total, int totalNumeric, String beforeTotal, String routes,
                                     String adult, int adultNumeric, String child, int childNumeric,
                                     String infant, int infantNumeric, String airline, int isRefundable) {
        this.id = id;
        this.flightType = flightType;
        this.term = term;
        this.aid = aid;
        this.departureAirport = departureAirport;
        this.departureTime = departureTime;
        this.departureTimeInt = departureTimeInt;
        this.arrivalAirport = arrivalAirport;
        this.arrivalTime = arrivalTime;
        this.arrivalTimeInt = arrivalTimeInt;
        this.totalTransit = totalTransit;
        this.addDayArrival = addDayArrival;
        this.duration = duration;
        this.durationMinute = durationMinute;
        this.total = total;
        this.totalNumeric = totalNumeric;
        this.beforeTotal = beforeTotal;
        this.routes = routes;
        this.adult = adult;
        this.adultNumeric = adultNumeric;
        this.child = child;
        this.childNumeric = childNumeric;
        this.infant = infant;
        this.infantNumeric = infantNumeric;
        this.airline = airline;
        this.isRefundable = isRefundable;
    }

    public FlightSearchSingleRouteDB() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlightType() {
        return flightType;
    }

    public void setFlightType(String flightType) {
        this.flightType = flightType;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getDepartureTimeInt() {
        return departureTimeInt;
    }

    public void setDepartureTimeInt(int departureTimeInt) {
        this.departureTimeInt = departureTimeInt;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getArrivalTimeInt() {
        return arrivalTimeInt;
    }

    public void setArrivalTimeInt(int arrivalTimeInt) {
        this.arrivalTimeInt = arrivalTimeInt;
    }

    public int getTotalTransit() {
        return totalTransit;
    }

    public void setTotalTransit(int totalTransit) {
        this.totalTransit = totalTransit;
    }

    public int getAddDayArrival() {
        return addDayArrival;
    }

    public void setAddDayArrival(int addDayArrival) {
        this.addDayArrival = addDayArrival;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getDurationMinute() {
        return durationMinute;
    }

    public void setDurationMinute(int durationMinute) {
        this.durationMinute = durationMinute;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getTotalNumeric() {
        return totalNumeric;
    }

    public void setTotalNumeric(int totalNumeric) {
        this.totalNumeric = totalNumeric;
    }

    public String getBeforeTotal() {
        return beforeTotal;
    }

    public void setBeforeTotal(String beforeTotal) {
        this.beforeTotal = beforeTotal;
    }

    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public int getAdultNumeric() {
        return adultNumeric;
    }

    public void setAdultNumeric(int adultNumeric) {
        this.adultNumeric = adultNumeric;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public int getChildNumeric() {
        return childNumeric;
    }

    public void setChildNumeric(int childNumeric) {
        this.childNumeric = childNumeric;
    }

    public String getInfant() {
        return infant;
    }

    public void setInfant(String infant) {
        this.infant = infant;
    }

    public int getInfantNumeric() {
        return infantNumeric;
    }

    public void setInfantNumeric(int infantNumeric) {
        this.infantNumeric = infantNumeric;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public int getIsRefundable() {
        return isRefundable;
    }

    public void setIsRefundable(int isRefundable) {
        this.isRefundable = isRefundable;
    }

    protected FlightSearchSingleRouteDB(Parcel in) {
        id = in.readString();
        flightType = in.readString();
        term = in.readString();
        aid = in.readString();
        departureAirport = in.readString();
        departureTime = in.readString();
        departureTimeInt = in.readInt();
        arrivalAirport = in.readString();
        arrivalTime = in.readString();
        arrivalTimeInt = in.readInt();
        totalTransit = in.readInt();
        addDayArrival = in.readInt();
        duration = in.readString();
        durationMinute = in.readInt();
        total = in.readString();
        totalNumeric = in.readInt();
        beforeTotal = in.readString();
        routes = in.readString();
        adult = in.readString();
        adultNumeric = in.readInt();
        child = in.readString();
        childNumeric = in.readInt();
        infant = in.readString();
        infantNumeric = in.readInt();
        airline = in.readString();
        isRefundable = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(flightType);
        dest.writeString(term);
        dest.writeString(aid);
        dest.writeString(departureAirport);
        dest.writeString(departureTime);
        dest.writeInt(departureTimeInt);
        dest.writeString(arrivalAirport);
        dest.writeString(arrivalTime);
        dest.writeInt(arrivalTimeInt);
        dest.writeInt(totalTransit);
        dest.writeInt(addDayArrival);
        dest.writeString(duration);
        dest.writeInt(durationMinute);
        dest.writeString(total);
        dest.writeInt(totalNumeric);
        dest.writeString(beforeTotal);
        dest.writeString(routes);
        dest.writeString(adult);
        dest.writeInt(adultNumeric);
        dest.writeString(child);
        dest.writeInt(childNumeric);
        dest.writeString(infant);
        dest.writeInt(infantNumeric);
        dest.writeString(airline);
        dest.writeInt(isRefundable);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightSearchSingleRouteDB> CREATOR = new Creator<FlightSearchSingleRouteDB>() {
        @Override
        public FlightSearchSingleRouteDB createFromParcel(Parcel in) {
            return new FlightSearchSingleRouteDB(in);
        }

        @Override
        public FlightSearchSingleRouteDB[] newArray(int size) {
            return new FlightSearchSingleRouteDB[size];
        }
    };

}
