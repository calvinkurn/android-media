package com.tokopedia.train.passenger.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.train.passenger.adapter.TrainBookingPassengerTypeFactory;

public class TrainPassengerViewModel implements Parcelable, Visitable<TrainBookingPassengerTypeFactory> {

    private int salutationId;
    private int paxType;
    private String name;
    private String identityNumber;
    private String birthdate;
    private String phone;
    private String headerTitle;
    private int passengerId;
    private String salutationTitle;

    public TrainPassengerViewModel() {
    }

    protected TrainPassengerViewModel(Parcel in) {
        salutationId = in.readInt();
        paxType = in.readInt();
        name = in.readString();
        identityNumber = in.readString();
        birthdate = in.readString();
        phone = in.readString();
        headerTitle = in.readString();
        passengerId = in.readInt();
        salutationTitle = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(salutationId);
        dest.writeInt(paxType);
        dest.writeString(name);
        dest.writeString(identityNumber);
        dest.writeString(birthdate);
        dest.writeString(phone);
        dest.writeString(headerTitle);
        dest.writeInt(passengerId);
        dest.writeString(salutationTitle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrainPassengerViewModel> CREATOR = new Creator<TrainPassengerViewModel>() {
        @Override
        public TrainPassengerViewModel createFromParcel(Parcel in) {
            return new TrainPassengerViewModel(in);
        }

        @Override
        public TrainPassengerViewModel[] newArray(int size) {
            return new TrainPassengerViewModel[size];
        }
    };

    public String getSalutationTitle() {
        return salutationTitle;
    }

    public void setSalutationTitle(String salutationTitle) {
        this.salutationTitle = salutationTitle;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public int getSalutationId() {
        return salutationId;
    }

    public void setSalutationId(int salutationId) {
        this.salutationId = salutationId;
    }

    public int getPaxType() {
        return paxType;
    }

    public void setPaxType(int paxType) {
        this.paxType = paxType;
    }

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

    @Override
    public int type(TrainBookingPassengerTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TrainPassengerViewModel && ((TrainPassengerViewModel) obj).getPassengerId() == passengerId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result *= prime * passengerId;
        return result;
    }
}
