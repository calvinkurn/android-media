package com.tokopedia.flight.booking.data.cloud.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.flight.search.data.cloud.model.response.Fare;

/**
 * @author by alvarisi on 11/15/17.
 */

public class NewFarePrice implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fare")
    @Expose
    private Fare fare;

    public NewFarePrice() {
    }

    public NewFarePrice(String id, Fare fare) {
        this.id = id;
        this.fare = fare;
    }

    protected NewFarePrice(Parcel in) {
        id = in.readString();
        fare = in.readParcelable(Fare.class.getClassLoader());
    }

    public static final Creator<NewFarePrice> CREATOR = new Creator<NewFarePrice>() {
        @Override
        public NewFarePrice createFromParcel(Parcel in) {
            return new NewFarePrice(in);
        }

        @Override
        public NewFarePrice[] newArray(int size) {
            return new NewFarePrice[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeParcelable(fare, i);
    }
}
