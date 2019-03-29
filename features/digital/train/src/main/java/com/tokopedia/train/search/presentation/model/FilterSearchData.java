package com.tokopedia.train.search.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 3/22/18.
 * This object contains all data filter search and selected data filter by user
 */

public class FilterSearchData implements Parcelable {

    private long minPrice;
    private long maxPrice;
    private List<String> departureTimeList;
    private List<String> trains;
    private List<String> trainClass;

    private long selectedMinPrice;
    private long selectedMaxPrice;
    private List<String> selectedDepartureTimeList;
    private List<String> selectedTrains;
    private List<String> selectedTrainClass;
    private boolean hasFilter;

    public FilterSearchData() {
    }

    protected FilterSearchData(Parcel in) {
        minPrice = in.readLong();
        maxPrice = in.readLong();
        departureTimeList = in.createStringArrayList();
        trains = in.createStringArrayList();
        trainClass = in.createStringArrayList();
        selectedMinPrice = in.readLong();
        selectedMaxPrice = in.readLong();
        selectedDepartureTimeList = in.createStringArrayList();
        selectedTrains = in.createStringArrayList();
        selectedTrainClass = in.createStringArrayList();
        hasFilter = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(minPrice);
        parcel.writeLong(maxPrice);
        parcel.writeStringList(departureTimeList);
        parcel.writeStringList(trains);
        parcel.writeStringList(trainClass);
        parcel.writeLong(selectedMinPrice);
        parcel.writeLong(selectedMaxPrice);
        parcel.writeStringList(selectedDepartureTimeList);
        parcel.writeStringList(selectedTrains);
        parcel.writeStringList(selectedTrainClass);
        parcel.writeByte((byte) (hasFilter ? 1 : 0));
    }

    public boolean isHasFilter() {
        return hasFilter;
    }

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

    public List<String> getDepartureTimeList() {
        return departureTimeList;
    }

    public void setDepartureTimeList(List<String> departureTimeList) {
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

    public List<String> getSelectedDepartureTimeList() {
        return selectedDepartureTimeList;
    }

    public void setSelectedDepartureTimeList(List<String> selectedDepartureTimeList) {
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

    public FilterSearchData copy(){
        FilterSearchData filterSearchData = new FilterSearchData();
        filterSearchData.setMinPrice(getMinPrice());
        filterSearchData.setMaxPrice(getMaxPrice());
        filterSearchData.setTrainClass(getTrainClass());
        filterSearchData.setTrains(getTrains());
        filterSearchData.setDepartureTimeList(getDepartureTimeList());

        filterSearchData.setSelectedMinPrice(getSelectedMinPrice());
        filterSearchData.setSelectedMaxPrice(getSelectedMaxPrice());
        filterSearchData.setSelectedDepartureTimeList(getSelectedDepartureTimeList());
        filterSearchData.setSelectedTrains(getSelectedTrains());
        filterSearchData.setSelectedTrainClass(getSelectedTrainClass());
        return filterSearchData;
    }

    public void setHasFilter(FilterSearchData filterSearchData) {
        this.hasFilter = filterSearchData.getSelectedMinPrice() >= 0 || filterSearchData.getSelectedMaxPrice() >= 0 ||
                         filterSearchData.getSelectedTrains() != null && !filterSearchData.getSelectedTrains().isEmpty() ||
                         filterSearchData.getSelectedTrainClass() != null && !filterSearchData.getSelectedTrainClass().isEmpty() ||
                         filterSearchData.getSelectedDepartureTimeList() != null && !filterSearchData.getSelectedDepartureTimeList().isEmpty();
    }

    public FilterSearchData resetSelectedValue() {
        FilterSearchData filterSearchData = new FilterSearchData();
        filterSearchData.setMinPrice(getMinPrice());
        filterSearchData.setMaxPrice(getMaxPrice());
        filterSearchData.setTrainClass(getTrainClass());
        filterSearchData.setTrains(getTrains());
        filterSearchData.setDepartureTimeList(getDepartureTimeList());

        filterSearchData.setSelectedMaxPrice(0);
        filterSearchData.setSelectedMinPrice(0);
        filterSearchData.setSelectedTrainClass(new ArrayList<>());
        filterSearchData.setSelectedTrains(new ArrayList<>());
        filterSearchData.setSelectedDepartureTimeList(new ArrayList<>());
        return filterSearchData;
    }
}
