package com.tokopedia.train.passenger.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainSeat implements Parcelable{
    private String klass;
    private String wagonNo;
    private String row;
    private String column;

    protected TrainSeat(Parcel in) {
        klass = in.readString();
        wagonNo = in.readString();
        row = in.readString();
        column = in.readString();
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

    public TrainSeat() {
    }

    public String getKlass() {
        return klass;
    }

    public String getWagonNo() {
        return wagonNo;
    }

    public String getRow() {
        return row;
    }

    public String getColumn() {
        return column;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(klass);
        parcel.writeString(wagonNo);
        parcel.writeString(row);
        parcel.writeString(column);
    }

    public void setKlass(String klass) {
        this.klass = klass;
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
