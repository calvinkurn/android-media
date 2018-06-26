package com.tokopedia.train.scheduledetail.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rizky on 07/06/18.
 */
public class TrainScheduleDetailViewModel implements Parcelable {

    private String originCityCode;
    private String destinationCityCode;
    private String originCityName;
    private String destinationCityName;
    private String originStationCode;
    private String originStationName;
    private String destinationStationCode;
    private String destinationStationName;
    private String duration;
    private String trainName;
    private String trainClass;
    private String departureDate;
    private String arrivalDate;
    private String departureTime;
    private String arrivalTime;
    private boolean isReturnTrip;
    private String displayAdultFare;
    private String displayInfantFare;

    TrainScheduleDetailViewModel(Builder builder) {
        this.originCityCode = builder.originCityCode;
        this.destinationCityCode = builder.destinationCityCode;
        this.originCityName = builder.originCityName;
        this.destinationCityName = builder.destinationCityName;
        this.originStationCode = builder.originStationCode;
        this.originStationName = builder.originStationName;
        this.destinationStationCode = builder.destinationStationCode;
        this.destinationStationName = builder.destinationStationName;
        this.duration = builder.duration;
        this.trainName = builder.trainName;
        this.trainClass = builder.trainClass;
        this.departureDate = builder.departureDate;
        this.arrivalDate = builder.arrivalDate;
        this.departureTime = builder.departureTime;
        this.arrivalTime = builder.arrivalTime;
        this.isReturnTrip = builder.isReturnTrip;
        this.displayAdultFare = builder.displayAdultFare;
        this.displayInfantFare = builder.displayInfantFare;
    }

    private TrainScheduleDetailViewModel(Parcel in) {
        originCityCode = in.readString();
        destinationCityCode = in.readString();
        originCityName = in.readString();
        destinationCityName = in.readString();
        originStationCode = in.readString();
        originStationName = in.readString();
        destinationStationCode = in.readString();
        destinationStationName = in.readString();
        duration = in.readString();
        trainName = in.readString();
        trainClass = in.readString();
        departureDate = in.readString();
        arrivalDate = in.readString();
        departureTime = in.readString();
        arrivalTime = in.readString();
        isReturnTrip = in.readByte() != 0;
        displayAdultFare = in.readString();
        displayInfantFare = in.readString();
    }

    public static final Creator<TrainScheduleDetailViewModel> CREATOR = new Creator<TrainScheduleDetailViewModel>() {
        @Override
        public TrainScheduleDetailViewModel createFromParcel(Parcel in) {
            return new TrainScheduleDetailViewModel(in);
        }

        @Override
        public TrainScheduleDetailViewModel[] newArray(int size) {
            return new TrainScheduleDetailViewModel[size];
        }
    };

    public String getOriginCityCode() {
        return originCityCode;
    }

    public String getDestinationCityCode() {
        return destinationCityCode;
    }

    public String getOriginCityName() {
        return originCityName;
    }

    public String getDestinationCityName() {
        return destinationCityName;
    }

    public String getOriginStationCode() {
        return originStationCode;
    }

    public String getOriginStationName() {
        return originStationName;
    }

    public String getDestinationStationCode() {
        return destinationStationCode;
    }

    public String getDestinationStationName() {
        return destinationStationName;
    }

    public String getDuration() {
        return duration;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getTrainClass() {
        return trainClass;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public boolean isReturnTrip() {
        return isReturnTrip;
    }

    public String getDisplayAdultFare() {
        return displayAdultFare;
    }

    public String getDisplayInfantFare() {
        return displayInfantFare;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originCityCode);
        dest.writeString(destinationCityCode);
        dest.writeString(originCityName);
        dest.writeString(destinationCityName);
        dest.writeString(originStationCode);
        dest.writeString(originStationName);
        dest.writeString(destinationStationCode);
        dest.writeString(destinationStationName);
        dest.writeString(duration);
        dest.writeString(trainName);
        dest.writeString(trainClass);
        dest.writeString(departureDate);
        dest.writeString(arrivalDate);
        dest.writeString(departureTime);
        dest.writeString(arrivalTime);
        dest.writeByte((byte) (isReturnTrip ? 1 : 0));
        dest.writeString(displayAdultFare);
    }

    public static class Builder {
        private String originCityCode;
        private String destinationCityCode;
        private String originCityName;
        private String destinationCityName;
        private String originStationCode;
        private String originStationName;
        private String destinationStationCode;
        private String destinationStationName;
        private String duration;
        private String trainName;
        private String trainClass;
        private String departureDate;
        private String arrivalDate;
        private String departureTime;
        private String arrivalTime;
        private boolean isReturnTrip;
        private String displayAdultFare;
        private String displayInfantFare;

        public Builder originCityCode(String val) {
            this.originCityCode = val;
            return this;
        }

        public Builder destinationCityCode(String val) {
            this.destinationCityCode = val;
            return this;
        }

        public Builder originCityName(String val) {
            this.originCityName = val;
            return this;
        }

        public Builder destinationCityName(String val) {
            this.destinationCityName = val;
            return this;
        }

        public Builder originStationCode(String val) {
            this.originStationCode = val;
            return this;
        }

        public Builder originStationName(String val) {
            this.originStationName = val;
            return this;
        }

        public Builder destinationStationCode(String val) {
            this.destinationStationCode = val;
            return this;
        }

        public Builder destinationStationName(String val) {
            this.destinationStationName = val;
            return this;
        }

        public Builder duration(String val) {
            this.duration = val;
            return this;
        }

        public Builder trainName(String val) {
            this.trainName = val;
            return this;
        }

        public Builder trainClass(String val) {
            this.trainClass = val;
            return this;
        }

        public Builder departureDate(String val) {
            this.departureDate = val;
            return this;
        }

        public Builder arrivalDate(String val) {
            this.arrivalDate = val;
            return this;
        }

        public Builder departureTime(String val) {
            this.departureTime = val;
            return this;
        }

        public Builder arrivalTime(String val) {
            this.arrivalTime = val;
            return this;
        }

        public Builder isReturnTrip(boolean val) {
            this.isReturnTrip = val;
            return this;
        }

        public Builder displayAdultFare(String val) {
            this.displayAdultFare = val;
            return this;
        }

        public Builder displayInfantFare(String val) {
            this.displayInfantFare = val;
            return this;
        }

        public TrainScheduleDetailViewModel build() {
            return new TrainScheduleDetailViewModel(this);
        }
    }

}
