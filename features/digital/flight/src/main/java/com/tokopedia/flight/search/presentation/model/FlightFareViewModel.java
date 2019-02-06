package com.tokopedia.flight.search.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rizky on 26/09/18.
 */
public class FlightFareViewModel implements Parcelable{

    private String adult;
    private String adultCombo;
    private String child;
    private String childCombo;
    private String infant;
    private String infantCombo;
    private int adultNumeric;
    private int adultNumericCombo;
    private int childNumeric;
    private int childNumericCombo;
    private int infantNumeric;
    private int infantNumericCombo;

    public FlightFareViewModel(String adult, String adultCombo, String child, String childCombo,
                               String infant, String infantCombo, int adultNumeric, int adultNumericCombo,
                               int childNumeric, int childNumericCombo, int infantNumeric, int infantNumericCombo) {
        this.adult = adult;
        this.adultCombo = adultCombo;
        this.child = child;
        this.childCombo = childCombo;
        this.infant = infant;
        this.infantCombo = infantCombo;
        this.adultNumeric = adultNumeric;
        this.adultNumericCombo = adultNumericCombo;
        this.childNumeric = childNumeric;
        this.childNumericCombo = childNumericCombo;
        this.infantNumeric = infantNumeric;
        this.infantNumericCombo = infantNumericCombo;
    }

    protected FlightFareViewModel(Parcel in) {
        adult = in.readString();
        adultCombo = in.readString();
        child = in.readString();
        childCombo = in.readString();
        infant = in.readString();
        infantCombo = in.readString();
        adultNumeric = in.readInt();
        adultNumericCombo = in.readInt();
        childNumeric = in.readInt();
        childNumericCombo = in.readInt();
        infantNumeric = in.readInt();
        infantNumericCombo = in.readInt();
    }

    public static final Creator<FlightFareViewModel> CREATOR = new Creator<FlightFareViewModel>() {
        @Override
        public FlightFareViewModel createFromParcel(Parcel in) {
            return new FlightFareViewModel(in);
        }

        @Override
        public FlightFareViewModel[] newArray(int size) {
            return new FlightFareViewModel[size];
        }
    };

    public String getAdult() {
        return adult;
    }

    public String getAdultCombo() {
        return adultCombo;
    }

    public String getChild() {
        return child;
    }

    public String getChildCombo() {
        return childCombo;
    }

    public String getInfant() {
        return infant;
    }

    public String getInfantCombo() {
        return infantCombo;
    }

    public int getAdultNumeric() {
        return adultNumeric;
    }

    public int getAdultNumericCombo() {
        return adultNumericCombo;
    }

    public int getChildNumeric() {
        return childNumeric;
    }

    public int getChildNumericCombo() {
        return childNumericCombo;
    }

    public int getInfantNumeric() {
        return infantNumeric;
    }

    public int getInfantNumericCombo() {
        return infantNumericCombo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(adult);
        parcel.writeString(adultCombo);
        parcel.writeString(child);
        parcel.writeString(childCombo);
        parcel.writeString(infant);
        parcel.writeString(infantCombo);
        parcel.writeInt(adultNumeric);
        parcel.writeInt(adultNumericCombo);
        parcel.writeInt(childNumeric);
        parcel.writeInt(childNumericCombo);
        parcel.writeInt(infantNumeric);
        parcel.writeInt(infantNumericCombo);
    }
}
