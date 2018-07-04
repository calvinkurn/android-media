package com.tokopedia.train.passenger.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainSeat implements Parcelable {
    private String klass;
    private String wagonNo;
    private String row;
    private String column;

    public TrainSeat(String klass, String wagonNo, String row, String column) {
        this.klass = klass;
        this.wagonNo = wagonNo;
        this.row = row;
        this.column = column;
    }

    protected TrainSeat(Parcel in) {
        klass = in.readString();
        wagonNo = in.readString();
        row = in.readString();
        column = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(klass);
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
}
