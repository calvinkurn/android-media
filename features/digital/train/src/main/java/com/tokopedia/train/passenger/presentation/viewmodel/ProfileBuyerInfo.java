package com.tokopedia.train.passenger.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 28/06/18.
 */
public class ProfileBuyerInfo implements Parcelable {

    private String fullname;
    private String email;
    private String phoneNumber;
    private String bday;
    private int gender;

    public ProfileBuyerInfo() {
    }

    protected ProfileBuyerInfo(Parcel in) {
        fullname = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        bday = in.readString();
        gender = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullname);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(bday);
        dest.writeInt(gender);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProfileBuyerInfo> CREATOR = new Creator<ProfileBuyerInfo>() {
        @Override
        public ProfileBuyerInfo createFromParcel(Parcel in) {
            return new ProfileBuyerInfo(in);
        }

        @Override
        public ProfileBuyerInfo[] newArray(int size) {
            return new ProfileBuyerInfo[size];
        }
    };

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
