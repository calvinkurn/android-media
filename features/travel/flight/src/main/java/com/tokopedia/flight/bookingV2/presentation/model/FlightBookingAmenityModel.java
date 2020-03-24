package com.tokopedia.flight.bookingV2.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.bookingV2.presentation.adapter.FlightAmenityAdapterTypeFactory;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingAmenityModel implements Parcelable, Visitable<FlightAmenityAdapterTypeFactory> {

    public static final Creator<FlightBookingAmenityModel> CREATOR = new Creator<FlightBookingAmenityModel>() {
        @Override
        public FlightBookingAmenityModel createFromParcel(Parcel in) {
            return new FlightBookingAmenityModel(in);
        }

        @Override
        public FlightBookingAmenityModel[] newArray(int size) {
            return new FlightBookingAmenityModel[size];
        }
    };
    private String id;
    private String title;
    private String price;
    private int priceNumeric;
    private String departureId;
    private String arrivalId;
    private int amenityType;

    public FlightBookingAmenityModel() {
    }

    protected FlightBookingAmenityModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        price = in.readString();
        priceNumeric = in.readInt();
        departureId = in.readString();
        arrivalId = in.readString();
        amenityType = in.readInt();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightBookingAmenityModel && ((FlightBookingAmenityModel) obj).getId().equalsIgnoreCase(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public int getPriceNumeric() {
        return priceNumeric;
    }

    public void setPriceNumeric(int priceNumeric) {
        this.priceNumeric = priceNumeric;
    }

    public String getDepartureId() {
        return departureId;
    }

    public void setDepartureId(String departureId) {
        this.departureId = departureId;
    }

    public String getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(String arrivalId) {
        this.arrivalId = arrivalId;
    }

    public int getAmenityType() {
        return amenityType;
    }

    public void setAmenityType(int amenityType) {
        this.amenityType = amenityType;
    }

    @Override
    public int type(FlightAmenityAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(price);
        parcel.writeInt(priceNumeric);
        parcel.writeString(departureId);
        parcel.writeString(arrivalId);
        parcel.writeInt(amenityType);
    }
}
