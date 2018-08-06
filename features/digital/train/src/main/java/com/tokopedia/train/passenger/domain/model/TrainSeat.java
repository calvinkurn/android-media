package com.tokopedia.train.passenger.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainSeat implements Parcelable {
    private String wagonNo;
    private String row;
    private String column;

    public TrainSeat(String wagonNo, String row, String column) {
        this.wagonNo = wagonNo;
        this.row = row;
        this.column = column;
    }

    public TrainSeat() {
    }

    protected TrainSeat(Parcel in) {
        wagonNo = in.readString();
        row = in.readString();
        column = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wagonNo);
        dest.writeString(row);
        dest.writeString(column);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrainSeat> CREATOR = new Creator<TrainSeat>() {
        @Override
        public TrainSeat createFromParcel(Parcel in) {
            return new TrainSeat(in);
        }

        @Override
        public TrainSeat[] newArray(int size) {
            return new TrainSeat[size];
        }
    };

    public String getWagonNo() {
        return wagonNo;
    }

    public String getRow() {
        return row;
    }

    public String getColumn() {
        return column;
    }

    public void setWagonNo(String wagonNo) {
        this.wagonNo = wagonNo;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public void setColumn(String column) {
        this.column = column;
    }

}
