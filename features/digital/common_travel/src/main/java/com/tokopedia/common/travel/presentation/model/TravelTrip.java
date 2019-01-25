package com.tokopedia.common.travel.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 22/11/18.
 */
public class TravelTrip implements Parcelable {

    private int travelPlatformType;
    private TravelPassenger travelPassengerBooking;
    private String upperBirthDate;
    private String lowerBirthDate;

    public TravelTrip() {
    }

    protected TravelTrip(Parcel in) {
        travelPlatformType = in.readInt();
        travelPassengerBooking = in.readParcelable(TravelPassenger.class.getClassLoader());
        upperBirthDate = in.readString();
        lowerBirthDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(travelPlatformType);
        dest.writeParcelable(travelPassengerBooking, flags);
        dest.writeString(upperBirthDate);
        dest.writeString(lowerBirthDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TravelTrip> CREATOR = new Creator<TravelTrip>() {
        @Override
        public TravelTrip createFromParcel(Parcel in) {
            return new TravelTrip(in);
        }

        @Override
        public TravelTrip[] newArray(int size) {
            return new TravelTrip[size];
        }
    };

    public String getUpperBirthDate() {
        return upperBirthDate;
    }

    public void setUpperBirthDate(String upperBirthDate) {
        this.upperBirthDate = upperBirthDate;
    }

    public String getLowerBirthDate() {
        return lowerBirthDate;
    }

    public void setLowerBirthDate(String lowerBirthDate) {
        this.lowerBirthDate = lowerBirthDate;
    }

    public int getTravelPlatformType() {
        return travelPlatformType;
    }

    public void setTravelPlatformType(int travelPlatformType) {
        this.travelPlatformType = travelPlatformType;
    }

    public TravelPassenger getTravelPassengerBooking() {
        return travelPassengerBooking;
    }

    public void setTravelPassengerBooking(TravelPassenger travelPassengerBooking) {
        this.travelPassengerBooking = travelPassengerBooking;
    }
}
