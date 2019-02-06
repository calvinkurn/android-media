package com.tokopedia.flight.search.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by User on 11/16/2017.
 */

public class FlightSearchSingleApiRequestModel implements Parcelable{
    private String depAirport;
    private String arrAirport;
    private String date;
    private int adult;
    private int children;
    private int infant;
    private int classID;
    private List<String> airlines;

    public FlightSearchSingleApiRequestModel(String depAirport, String arrAirport,
                                             String date, int adult, int children, int infant, int classID,
                                             List<String> airlines) {
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
        this.date = date;
        this.adult = adult;
        this.children = children;
        this.infant = infant;
        this.classID = classID;
        this.airlines = airlines;
    }

    protected FlightSearchSingleApiRequestModel(Parcel in) {
        depAirport = in.readString();
        arrAirport = in.readString();
        date = in.readString();
        adult = in.readInt();
        children = in.readInt();
        infant = in.readInt();
        classID = in.readInt();
        airlines = in.createStringArrayList();
    }

    public static final Creator<FlightSearchSingleApiRequestModel> CREATOR = new Creator<FlightSearchSingleApiRequestModel>() {
        @Override
        public FlightSearchSingleApiRequestModel createFromParcel(Parcel in) {
            return new FlightSearchSingleApiRequestModel(in);
        }

        @Override
        public FlightSearchSingleApiRequestModel[] newArray(int size) {
            return new FlightSearchSingleApiRequestModel[size];
        }
    };

    public String getDepAirport() {
        return depAirport;
    }

    public String getArrAirport() {
        return arrAirport;
    }

    public String getDate() {
        return date;
    }

    public int getAdult() {
        return adult;
    }

    public int getChildren() {
        return children;
    }

    public int getInfant() {
        return infant;
    }

    public int getClassID() {
        return classID;
    }

    public List<String> getAirlines() {
        return airlines;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(depAirport);
        parcel.writeString(arrAirport);
        parcel.writeString(date);
        parcel.writeInt(adult);
        parcel.writeInt(children);
        parcel.writeInt(infant);
        parcel.writeInt(classID);
        parcel.writeStringList(airlines);
    }
}
