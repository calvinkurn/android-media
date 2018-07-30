package com.tokopedia.train.passenger.domain.requestmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 24/07/18.
 */
public class TrainPassengerRequest implements Parcelable {

    private String idNumber;
    private String name;
    private int paxType;
    private int salutationId;
    private String phoneNumber;

    public TrainPassengerRequest() {
    }

    protected TrainPassengerRequest(Parcel in) {
        idNumber = in.readString();
        name = in.readString();
        paxType = in.readInt();
        salutationId = in.readInt();
        phoneNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idNumber);
        dest.writeString(name);
        dest.writeInt(paxType);
        dest.writeInt(salutationId);
        dest.writeString(phoneNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrainPassengerRequest> CREATOR = new Creator<TrainPassengerRequest>() {
        @Override
        public TrainPassengerRequest createFromParcel(Parcel in) {
            return new TrainPassengerRequest(in);
        }

        @Override
        public TrainPassengerRequest[] newArray(int size) {
            return new TrainPassengerRequest[size];
        }
    };

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPaxType(int paxType) {
        this.paxType = paxType;
    }

    public void setSalutationId(int salutationId) {
        this.salutationId = salutationId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getName() {
        return name;
    }

    public int getPaxType() {
        return paxType;
    }

    public int getSalutationId() {
        return salutationId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
