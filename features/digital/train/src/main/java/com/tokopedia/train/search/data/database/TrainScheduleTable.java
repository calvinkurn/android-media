package com.tokopedia.train.search.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by nabillasabbaha on 01/02/19.
 */
@Entity
public class TrainScheduleTable {

    @PrimaryKey
    @NonNull
    private String idSchedule;
    private long adultFare;
    private String displayAdultFare;
    private long infantFare;
    private String displayInfantFare;
    private String arrivalTimestamp;
    private String departureTimestamp;
    private int departureHour;
    private long departureTime;
    private String classTrain;
    private String displayClass;
    private String subclass;
    private String origin;
    private String destination;
    private String displayDuration;
    private int duration;
    private String trainKey;
    private String trainName;
    private String trainNumber;
    private int availableSeat;
    private boolean cheapestFlag;
    private boolean fastestFlag;
    private boolean isReturnSchedule;
    private int minAdultFare;
    private int minDuration;

    @NonNull
    public String getIdSchedule() {
        return idSchedule;
    }

    public long getAdultFare() {
        return adultFare;
    }

    public String getDisplayAdultFare() {
        return displayAdultFare;
    }

    public long getInfantFare() {
        return infantFare;
    }

    public String getDisplayInfantFare() {
        return displayInfantFare;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public int getDepartureHour() {
        return departureHour;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public String getClassTrain() {
        return classTrain;
    }

    public String getDisplayClass() {
        return displayClass;
    }

    public String getSubclass() {
        return subclass;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDisplayDuration() {
        return displayDuration;
    }

    public int getDuration() {
        return duration;
    }

    public String getTrainKey() {
        return trainKey;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public int getAvailableSeat() {
        return availableSeat;
    }

    public int getMinAdultFare() {
        return minAdultFare;
    }

    public int getMinDuration() {
        return minDuration;
    }

    public boolean isCheapestFlag() {
        return cheapestFlag;
    }

    public boolean isFastestFlag() {
        return fastestFlag;
    }

    public boolean isReturnSchedule() {
        return isReturnSchedule;
    }

    public void setIdSchedule(@NonNull String idSchedule) {
        this.idSchedule = idSchedule;
    }

    public void setAdultFare(long adultFare) {
        this.adultFare = adultFare;
    }

    public void setDisplayAdultFare(String displayAdultFare) {
        this.displayAdultFare = displayAdultFare;
    }

    public void setInfantFare(long infantFare) {
        this.infantFare = infantFare;
    }

    public void setDisplayInfantFare(String displayInfantFare) {
        this.displayInfantFare = displayInfantFare;
    }

    public void setArrivalTimestamp(String arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public void setDepartureTimestamp(String departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }

    public void setDepartureHour(int departureHour) {
        this.departureHour = departureHour;
    }

    public void setDepartureTime(long departureTime) {
        this.departureTime = departureTime;
    }

    public void setClassTrain(String classTrain) {
        this.classTrain = classTrain;
    }

    public void setDisplayClass(String displayClass) {
        this.displayClass = displayClass;
    }

    public void setSubclass(String subclass) {
        this.subclass = subclass;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDisplayDuration(String displayDuration) {
        this.displayDuration = displayDuration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTrainKey(String trainKey) {
        this.trainKey = trainKey;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public void setAvailableSeat(int availableSeat) {
        this.availableSeat = availableSeat;
    }

    public void setCheapestFlag(boolean cheapestFlag) {
        this.cheapestFlag = cheapestFlag;
    }

    public void setFastestFlag(boolean fastestFlag) {
        this.fastestFlag = fastestFlag;
    }

    public void setReturnSchedule(boolean returnSchedule) {
        isReturnSchedule = returnSchedule;
    }

    public void setMinAdultFare(int minAdultFare) {
        this.minAdultFare = minAdultFare;
    }

    public void setMinDuration(int minDuration) {
        this.minDuration = minDuration;
    }
}
