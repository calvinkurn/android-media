package com.tokopedia.train.passenger.domain.requestmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 24/07/18.
 */
public class TrainScheduleRequest implements Parcelable {

    private String origin;
    private String destination;
    private String trainName;
    private String trainNo;
    private String trainClass;
    private String subclass;
    private String departureTimestamp;

    public TrainScheduleRequest() {
    }

    protected TrainScheduleRequest(Parcel in) {
        origin = in.readString();
        destination = in.readString();
        trainName = in.readString();
        trainNo = in.readString();
        trainClass = in.readString();
        subclass = in.readString();
        departureTimestamp = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(origin);
        dest.writeString(destination);
        dest.writeString(trainName);
        dest.writeString(trainNo);
        dest.writeString(trainClass);
        dest.writeString(subclass);
        dest.writeString(departureTimestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrainScheduleRequest> CREATOR = new Creator<TrainScheduleRequest>() {
        @Override
        public TrainScheduleRequest createFromParcel(Parcel in) {
            return new TrainScheduleRequest(in);
        }

        @Override
        public TrainScheduleRequest[] newArray(int size) {
            return new TrainScheduleRequest[size];
        }
    };

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public void setTrainClass(String trainClass) {
        this.trainClass = trainClass;
    }

    public void setSubClass(String subclass) {
        this.subclass = subclass;
    }

    public void setDepartureTimestamp(String departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public String getTrainClass() {
        return trainClass;
    }

    public String getSubclass() {
        return subclass;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }
}
