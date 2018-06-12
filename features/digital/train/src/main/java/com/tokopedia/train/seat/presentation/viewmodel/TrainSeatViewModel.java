package com.tokopedia.train.seat.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainSeatViewModel implements Parcelable{
    private String column;
    private int row;
    private int status;

    public TrainSeatViewModel() {
    }

    protected TrainSeatViewModel(Parcel in) {
        column = in.readString();
        row = in.readInt();
        status = in.readInt();
    }

    public static final Creator<TrainSeatViewModel> CREATOR = new Creator<TrainSeatViewModel>() {
        @Override
        public TrainSeatViewModel createFromParcel(Parcel in) {
            return new TrainSeatViewModel(in);
        }

        @Override
        public TrainSeatViewModel[] newArray(int size) {
            return new TrainSeatViewModel[size];
        }
    };

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(column);
        parcel.writeInt(row);
        parcel.writeInt(status);
    }
}
