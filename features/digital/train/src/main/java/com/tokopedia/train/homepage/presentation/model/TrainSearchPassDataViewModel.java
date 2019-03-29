package com.tokopedia.train.homepage.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by alvarisi on 3/8/18.
 */

public class TrainSearchPassDataViewModel implements Parcelable {
    private int adult;
    private int infant;
    private String departureDate;
    private String returnDate;
    private String originStationCode;
    private String originCityName;
    private String destinationStationCode;
    private String destinationCityName;
    private boolean isOneWay;

    public TrainSearchPassDataViewModel(int adult, int infant,
                                        String departureDate, String returnDate,
                                        String originStationCode, String originCityName,
                                        String destinationStationCode, String destinationCityName,
                                        boolean isOneWay) {
        this.adult = adult;
        this.infant = infant;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.originStationCode = originStationCode;
        this.originCityName = originCityName;
        this.destinationStationCode = destinationStationCode;
        this.destinationCityName = destinationCityName;
        this.isOneWay = isOneWay;
    }

    protected TrainSearchPassDataViewModel(Parcel in) {
        adult = in.readInt();
        infant = in.readInt();
        departureDate = in.readString();
        returnDate = in.readString();
        originStationCode = in.readString();
        originCityName = in.readString();
        destinationStationCode = in.readString();
        destinationCityName = in.readString();
        isOneWay = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(adult);
        dest.writeInt(infant);
        dest.writeString(departureDate);
        dest.writeString(returnDate);
        dest.writeString(originStationCode);
        dest.writeString(originCityName);
        dest.writeString(destinationStationCode);
        dest.writeString(destinationCityName);
        dest.writeByte((byte) (isOneWay ? 1 : 0));
    }

    public static final Creator<TrainSearchPassDataViewModel> CREATOR = new Creator<TrainSearchPassDataViewModel>() {
        @Override
        public TrainSearchPassDataViewModel createFromParcel(Parcel in) {
            return new TrainSearchPassDataViewModel(in);
        }

        @Override
        public TrainSearchPassDataViewModel[] newArray(int size) {
            return new TrainSearchPassDataViewModel[size];
        }
    };

    public int getAdult() {
        return adult;
    }

    public int getInfant() {
        return infant;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getOriginStationCode() {
        return originStationCode;
    }

    public String getOriginCityName() {
        return originCityName;
    }

    public String getDestinationStationCode() {
        return destinationStationCode;
    }

    public String getDestinationCityName() {
        return destinationCityName;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
