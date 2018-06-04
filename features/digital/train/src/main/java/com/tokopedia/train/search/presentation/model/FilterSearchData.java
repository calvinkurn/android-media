package com.tokopedia.train.search.presentation.model;

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

    private long selectedMinPrice;
    private long selectedMaxPrice;
    private String[] selectedDepartureTimeList;
    private List<String> selectedTrains;
    private List<String> selectedTrainClass;

    public FilterSearchData() {
    }


    protected FilterSearchData(Parcel in) {
        minPrice = in.readLong();
        maxPrice = in.readLong();
        departureTimeList = in.createStringArray();
        trains = in.createStringArrayList();
        trainClass = in.createStringArrayList();
        selectedMinPrice = in.readLong();
        selectedMaxPrice = in.readLong();
        selectedDepartureTimeList = in.createStringArray();
        selectedTrains = in.createStringArrayList();
        selectedTrainClass = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(minPrice);
        dest.writeLong(maxPrice);
        dest.writeStringArray(departureTimeList);
        dest.writeStringList(trains);
        dest.writeStringList(trainClass);
        dest.writeLong(selectedMinPrice);
        dest.writeLong(selectedMaxPrice);
        dest.writeStringArray(selectedDepartureTimeList);
        dest.writeStringList(selectedTrains);
        dest.writeStringList(selectedTrainClass);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public long getSelectedMinPrice() {
        return selectedMinPrice;
    }

    public void setSelectedMinPrice(long selectedMinPrice) {
        this.selectedMinPrice = selectedMinPrice;
    }

    public long getSelectedMaxPrice() {
        return selectedMaxPrice;
    }

    public void setSelectedMaxPrice(long selectedMaxPrice) {
        this.selectedMaxPrice = selectedMaxPrice;
    }

    public String[] getSelectedDepartureTimeList() {
        return selectedDepartureTimeList;
    }

    public void setSelectedDepartureTimeList(String[] selectedDepartureTimeList) {
        this.selectedDepartureTimeList = selectedDepartureTimeList;
    }

    public List<String> getSelectedTrains() {
        return selectedTrains;
    }

    public void setSelectedTrains(List<String> selectedTrains) {
        this.selectedTrains = selectedTrains;
    }

    public List<String> getSelectedTrainClass() {
        return selectedTrainClass;
    }

    public void setSelectedTrainClass(List<String> selectedTrainClass) {
        this.selectedTrainClass = selectedTrainClass;
    }
}
