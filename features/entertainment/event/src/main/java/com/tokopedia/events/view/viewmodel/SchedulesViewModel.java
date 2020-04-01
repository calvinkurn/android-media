package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SchedulesViewModel implements Parcelable {

    private int startDate;
    private int endDate;
    private String timeRange;
    private String aDdress;
    private String cityName;
    private List<PackageViewModel> packages = null;

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public String getaDdress() {
        return aDdress;
    }

    public void setaDdress(String aDdress) {
        this.aDdress = aDdress;
    }


    public List<PackageViewModel> getPackages() {
        return packages;
    }


    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public void setPackages(List<PackageViewModel> packages) {
        this.packages = packages;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public SchedulesViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.startDate);
        dest.writeInt(this.endDate);
        dest.writeString(this.timeRange);
        dest.writeString(this.aDdress);
        dest.writeString(this.cityName);
        dest.writeTypedList(this.packages);
    }

    protected SchedulesViewModel(Parcel in) {
        this.startDate = in.readInt();
        this.endDate = in.readInt();
        this.timeRange = in.readString();
        this.aDdress = in.readString();
        this.cityName = in.readString();
        this.packages = in.createTypedArrayList(PackageViewModel.CREATOR);
    }

    public static final Creator<SchedulesViewModel> CREATOR = new Creator<SchedulesViewModel>() {
        @Override
        public SchedulesViewModel createFromParcel(Parcel source) {
            return new SchedulesViewModel(source);
        }

        @Override
        public SchedulesViewModel[] newArray(int size) {
            return new SchedulesViewModel[size];
        }
    };
}