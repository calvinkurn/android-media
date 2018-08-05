package com.tokopedia.train.seat.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainSeatPassengerViewModel implements Parcelable{
    private int passengerNumber;
    private String name;
    private String number;
    private TrainSeatPassengerSeatViewModel seatViewModel;

    public TrainSeatPassengerViewModel() {
    }

    protected TrainSeatPassengerViewModel(Parcel in) {
        passengerNumber = in.readInt();
        name = in.readString();
        number = in.readString();
    }

    public static final Creator<TrainSeatPassengerViewModel> CREATOR = new Creator<TrainSeatPassengerViewModel>() {
        @Override
        public TrainSeatPassengerViewModel createFromParcel(Parcel in) {
            return new TrainSeatPassengerViewModel(in);
        }

        @Override
        public TrainSeatPassengerViewModel[] newArray(int size) {
            return new TrainSeatPassengerViewModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public TrainSeatPassengerSeatViewModel getSeatViewModel() {
        return seatViewModel;
    }

    public void setSeatViewModel(TrainSeatPassengerSeatViewModel seatViewModel) {
        this.seatViewModel = seatViewModel;
    }

    public int getPassengerNumber() {
        return passengerNumber;
    }

    public void setPassengerNumber(int passengerNumber) {
        this.passengerNumber = passengerNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(passengerNumber);
        parcel.writeString(name);
        parcel.writeString(number);
    }
}
