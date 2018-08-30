package com.tokopedia.common.travel.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TravelPassenger implements Parcelable {

    private int id;
    private int userId;
    private int salutationId;
    private int paxType;
    private String name;
    private String identityNumber;
    private String birthDate;
    private String phoneNumber;
    private String headerTitle;
    private int passengerId;
    private String salutationTitle;
    private int isBuyer;

    public TravelPassenger() {
    }

    protected TravelPassenger(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        salutationId = in.readInt();
        paxType = in.readInt();
        name = in.readString();
        identityNumber = in.readString();
        birthDate = in.readString();
        phoneNumber = in.readString();
        headerTitle = in.readString();
        passengerId = in.readInt();
        salutationTitle = in.readString();
        isBuyer = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(salutationId);
        dest.writeInt(paxType);
        dest.writeString(name);
        dest.writeString(identityNumber);
        dest.writeString(birthDate);
        dest.writeString(phoneNumber);
        dest.writeString(headerTitle);
        dest.writeInt(passengerId);
        dest.writeString(salutationTitle);
        dest.writeInt(isBuyer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TravelPassenger> CREATOR = new Creator<TravelPassenger>() {
        @Override
        public TravelPassenger createFromParcel(Parcel in) {
            return new TravelPassenger(in);
        }

        @Override
        public TravelPassenger[] newArray(int size) {
            return new TravelPassenger[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getSalutationTitle() {
        return salutationTitle;
    }

    public void setSalutationTitle(String salutationTitle) {
        this.salutationTitle = salutationTitle;
    }

    public int getIsBuyer() {
        return isBuyer;
    }

    public void setIsBuyer(int isBuyer) {
        this.isBuyer = isBuyer;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TravelPassenger && ((TravelPassenger) obj).getPassengerId() == passengerId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result *= prime * passengerId;
        return result;
    }
}
