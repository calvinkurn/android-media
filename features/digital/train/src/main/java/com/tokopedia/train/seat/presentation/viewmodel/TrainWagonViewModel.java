package com.tokopedia.train.seat.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TrainWagonViewModel implements Parcelable {
    private String wagonCode;
    private List<TrainSeatViewModel> seats;

    public TrainWagonViewModel() {
    }

    protected TrainWagonViewModel(Parcel in) {
        wagonCode = in.readString();
        seats = in.createTypedArrayList(TrainSeatViewModel.CREATOR);
    }

    public static final Creator<TrainWagonViewModel> CREATOR = new Creator<TrainWagonViewModel>() {
        @Override
        public TrainWagonViewModel createFromParcel(Parcel in) {
            return new TrainWagonViewModel(in);
        }

        @Override
        public TrainWagonViewModel[] newArray(int size) {
            return new TrainWagonViewModel[size];
        }
    };

    public String getWagonCode() {
        return wagonCode;
    }

    public void setWagonCode(String wagonCode) {
        this.wagonCode = wagonCode;
    }

    public List<TrainSeatViewModel> getSeats() {
        return seats;
    }

    public void setSeats(List<TrainSeatViewModel> seats) {
        this.seats = seats;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(wagonCode);
        parcel.writeTypedList(seats);
    }
}
