package com.tokopedia.train.passenger.domain.requestmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 24/07/18.
 */
public class TrainBuyerRequest implements Parcelable {

    private String name;
    private String phone;
    private String email;

    public TrainBuyerRequest() {
    }

    protected TrainBuyerRequest(Parcel in) {
        name = in.readString();
        phone = in.readString();
        email = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrainBuyerRequest> CREATOR = new Creator<TrainBuyerRequest>() {
        @Override
        public TrainBuyerRequest createFromParcel(Parcel in) {
            return new TrainBuyerRequest(in);
        }

        @Override
        public TrainBuyerRequest[] newArray(int size) {
            return new TrainBuyerRequest[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
