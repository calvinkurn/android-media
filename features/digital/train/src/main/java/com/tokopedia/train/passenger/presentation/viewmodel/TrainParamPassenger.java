package com.tokopedia.train.passenger.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nabillasabbaha on 26/06/18.
 */
public class TrainParamPassenger implements Parcelable {

    private String name;
    private String identityNumber;
    private String birthdate;
    private String phone;
    private String email;
    private List<TrainPassengerViewModel> trainPassengerViewModelList;

    public TrainParamPassenger() {
    }

    protected TrainParamPassenger(Parcel in) {
        name = in.readString();
        identityNumber = in.readString();
        birthdate = in.readString();
        phone = in.readString();
        email = in.readString();
        trainPassengerViewModelList = in.createTypedArrayList(TrainPassengerViewModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(identityNumber);
        dest.writeString(birthdate);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeTypedList(trainPassengerViewModelList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrainParamPassenger> CREATOR = new Creator<TrainParamPassenger>() {
        @Override
        public TrainParamPassenger createFromParcel(Parcel in) {
            return new TrainParamPassenger(in);
        }

        @Override
        public TrainParamPassenger[] newArray(int size) {
            return new TrainParamPassenger[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TrainPassengerViewModel> getTrainPassengerViewModelList() {
        return trainPassengerViewModelList;
    }

    public void setTrainPassengerViewModelList(List<TrainPassengerViewModel> trainPassengerViewModelList) {
        this.trainPassengerViewModelList = trainPassengerViewModelList;
    }
}
