package com.tokopedia.train.search.domain;

import java.util.List;

/**
 * @author rizkyfadillah on 15/03/18.
 */

public class FilterParam {

    private long minPrice;
    private long maxPrice;
    private List<String> departureTimeList;
    private List<String> trains;
    private List<String> trainClass;
    private String arrivalTimestampSelected;
    private int scheduleVariant;

    private FilterParam(Builder builder) {
        this.setMinPrice(builder.minPrice);
        this.setMaxPrice(builder.maxPrice);
        this.setTrains(builder.trains);
        this.setTrainClass(builder.trainClass);
        this.setDepartureTimeList(builder.departureTimeList);
        this.setArrivalTimestampSelected(builder.arrivalTimestampSelected);
        this.setScheduleVariant(builder.scheduleVariant);
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

    public List<String> getTrains() {
        return trains;
    }

    public void setTrains(List<String> trains) {
        this.trains = trains;
    }

    public List<String> getDepartureTimeList() {
        return departureTimeList;
    }

    public void setDepartureTimeList(List<String> departureTimeList) {
        this.departureTimeList = departureTimeList;
    }

    public List<String> getTrainClass() {
        return trainClass;
    }

    public void setTrainClass(List<String> trainClass) {
        this.trainClass = trainClass;
    }

    public String getArrivalTimestampSelected() {
        return arrivalTimestampSelected;
    }

    public void setArrivalTimestampSelected(String arrivalTimestampSelected) {
        this.arrivalTimestampSelected = arrivalTimestampSelected;
    }

    public int getScheduleVariant() {
        return scheduleVariant;
    }

    public void setScheduleVariant(int scheduleVariant) {
        this.scheduleVariant = scheduleVariant;
    }

    public static class Builder {

        private long minPrice;
        private long maxPrice;
        private List<String> departureTimeList;
        private List<String> trains;
        private List<String> trainClass;
        private String arrivalTimestampSelected;
        private int scheduleVariant;

        public Builder minPrice(long minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public Builder maxPrice(long maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public Builder trains(List<String> trains) {
            this.trains = trains;
            return this;
        }

        public Builder trainClass(List<String> trainClass) {
            this.trainClass = trainClass;
            return this;
        }

        public Builder departureTimeList(List<String> departureTimeList) {
            this.departureTimeList = departureTimeList;
            return this;
        }

        public Builder arrivalTimestampSelected(String arrivalTimestampSelected) {
            this.arrivalTimestampSelected = arrivalTimestampSelected;
            return this;
        }

        public Builder scheduleVariant(int scheduleVariant) {
            this.scheduleVariant = scheduleVariant;
            return this;
        }

        public FilterParam build() {
            return new FilterParam(this);
        }

    }

}
