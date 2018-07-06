package com.tokopedia.train.passenger.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainPaxPassenger implements Parcelable{
    private String name;
    private String idNumber;
    private int paxType;
    private TrainSeat seat;

    public TrainPaxPassenger() {
    }

    protected TrainPaxPassenger(Parcel in) {
        name = in.readString();
        idNumber = in.readString();
        paxType = in.readInt();
        seat = in.readParcelable(TrainSeat.class.getClassLoader());
    }

    public static final Creator<TrainPaxPassenger> CREATOR = new Creator<TrainPaxPassenger>() {
        @Override
        public TrainPaxPassenger createFromParcel(Parcel in) {
            return new TrainPaxPassenger(in);
        }

        @Override
        public TrainPaxPassenger[] newArray(int size) {
            return new TrainPaxPassenger[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getPaxType() {
        return paxType;
    }

    public TrainSeat getSeat() {
        return seat;
    }

    public String getIdNumber() {
        return idNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(idNumber);
        parcel.writeInt(paxType);
        parcel.writeParcelable(seat, i);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public void setPaxType(int paxType) {
        this.paxType = paxType;
    }

    public void setSeat(TrainSeat seat) {
        this.seat = seat;
    }
}
