package com.tokopedia.train.passenger.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainPaxPassenger implements Parcelable {
    private String name;
    private String idNumber;
    private int paxType;
    private TrainSeat seat;

    public TrainPaxPassenger(String name, String idNumber, int paxType, TrainSeat seat) {
        this.name = name;
        this.idNumber = idNumber;
        this.paxType = paxType;
        this.seat = seat;
    }

    public TrainPaxPassenger() {}

    protected TrainPaxPassenger(Parcel in) {
        name = in.readString();
        idNumber = in.readString();
        paxType = in.readInt();
        seat = in.readParcelable(TrainSeat.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(idNumber);
        dest.writeInt(paxType);
        dest.writeParcelable(seat, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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
