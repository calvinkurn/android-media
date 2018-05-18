package com.tokopedia.travelcalendar.view.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HolidayResult implements Parcelable {

    private String id;
    private HolidayDetail attributes;

    public HolidayResult() {
    }

    protected HolidayResult(Parcel in) {
        id = in.readString();
        attributes = in.readParcelable(HolidayDetail.class.getClassLoader());
    }

    public static final Creator<HolidayResult> CREATOR = new Creator<HolidayResult>() {
        @Override
        public HolidayResult createFromParcel(Parcel in) {
            return new HolidayResult(in);
        }

        @Override
        public HolidayResult[] newArray(int size) {
            return new HolidayResult[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public void setAttributes(HolidayDetail attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return this.id;
    }

    public HolidayDetail getAttributes() {
        return this.attributes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeParcelable(attributes, i);
    }
}
