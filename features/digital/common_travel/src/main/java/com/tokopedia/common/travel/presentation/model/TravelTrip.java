package com.tokopedia.common.travel.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 22/11/18.
 */
public class TravelTrip implements Parcelable {

    private int travelPlatformType;
    private TravelPassenger travelPassengerBooking;

    public TravelTrip() {
    }

    protected TravelTrip(Parcel in) {
        travelPlatformType = in.readInt();
        travelPassengerBooking = in.readParcelable(TravelPassenger.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(travelPlatformType);
        dest.writeParcelable(travelPassengerBooking, flags);
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
