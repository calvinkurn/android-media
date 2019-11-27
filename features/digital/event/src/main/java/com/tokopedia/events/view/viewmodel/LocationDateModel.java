package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by pranaymohapatra on 23/02/18.
 */

public class LocationDateModel implements Parcelable {
    private String mLocation;
    private String date;

    protected LocationDateModel(Parcel in) {
        mLocation = in.readString();
    }

    public LocationDateModel() {
    }

    public static final Creator<LocationDateModel> CREATOR = new Creator<LocationDateModel>() {
        @Override
        public LocationDateModel createFromParcel(Parcel in) {
            return new LocationDateModel(in);
        }

        @Override
        public LocationDateModel[] newArray(int size) {
            return new LocationDateModel[size];
        }
    };

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLocation);
    }
}
