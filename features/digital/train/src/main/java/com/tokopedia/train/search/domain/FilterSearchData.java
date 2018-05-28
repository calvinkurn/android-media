package com.tokopedia.train.search.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/22/18.
 */

public class FilterSearchData implements Parcelable {

    private long minPrice;
    private long maxPrice;
    private String[] departureTimeList;
    private List<String> trains;
    private List<String> trainClass;

    public FilterSearchData() {
    }

    protected FilterSearchData(Parcel in) {
        minPrice = in.readLong();
        maxPrice = in.readLong();
        departureTimeList = in.createStringArray();
        trains = in.createStringArrayList();
        trainClass = in.createStringArrayList();
    }

    public static final Creator<FilterSearchData> CREATOR = new Creator<FilterSearchData>() {
        @Override
        public FilterSearchData createFromParcel(Parcel in) {
            return new FilterSearchData(in);
        }

        @Override
        public FilterSearchData[] newArray(int size) {
            return new FilterSearchData[size];
        }
    };

    public long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(long minPrice) {
        this.minPrice = minPrice;
    }

    public long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(long maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String[] getDepartureTimeList() {
        return departureTimeList;
    }

    public void setDepartureTimeList(String[] departureTimeList) {
        this.departureTimeList = departureTimeList;
    }

    public List<String> getTrains() {
        return trains;
    }

    public void setTrains(List<String> trains) {
        this.trains = trains;
    }

    public List<String> getTrainClass() {
        return trainClass;
    }

    public void setTrainClass(List<String> trainClass) {
        this.trainClass = trainClass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(minPrice);
        parcel.writeLong(maxPrice);
        parcel.writeStringArray(departureTimeList);
        parcel.writeStringList(trains);
        parcel.writeStringList(trainClass);
    }
}
