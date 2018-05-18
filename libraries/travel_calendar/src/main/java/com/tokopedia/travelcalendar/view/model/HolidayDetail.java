package com.tokopedia.travelcalendar.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class HolidayDetail implements Parcelable {

    private String date;
    private String label;
    private Date dateHoliday;

    public HolidayDetail() {
    }

    protected HolidayDetail(Parcel in) {
        date = in.readString();
        label = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(label);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HolidayDetail> CREATOR = new Creator<HolidayDetail>() {
        @Override
        public HolidayDetail createFromParcel(Parcel in) {
            return new HolidayDetail(in);
        }

        @Override
        public HolidayDetail[] newArray(int size) {
            return new HolidayDetail[size];
        }
    };

    public void setDate(String date) {
        this.date = date;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDate() {
        return this.date;
    }

    public String getLabel() {
        return this.label;
    }

    public Date getDateHoliday() {
        return dateHoliday;
    }

    public void setDateHoliday(Date dateHoliday) {
        this.dateHoliday = dateHoliday;
    }
}
