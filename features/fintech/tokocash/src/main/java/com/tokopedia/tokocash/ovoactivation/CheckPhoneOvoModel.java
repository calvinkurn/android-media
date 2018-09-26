package com.tokopedia.tokocash.ovoactivation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 24/09/18.
 */
public class CheckPhoneOvoModel implements Parcelable {

    private String phoneNumber;
    private boolean isRegistered;
    private String registeredApplink;
    private String notRegisteredApplink;
    private String changeMsisdnApplink;

    public CheckPhoneOvoModel() {
    }

    protected CheckPhoneOvoModel(Parcel in) {
        phoneNumber = in.readString();
        isRegistered = in.readByte() != 0;
        registeredApplink = in.readString();
        notRegisteredApplink = in.readString();
        changeMsisdnApplink = in.readString();
    }

    public static final Creator<CheckPhoneOvoModel> CREATOR = new Creator<CheckPhoneOvoModel>() {
        @Override
        public CheckPhoneOvoModel createFromParcel(Parcel in) {
            return new CheckPhoneOvoModel(in);
        }

        @Override
        public CheckPhoneOvoModel[] newArray(int size) {
            return new CheckPhoneOvoModel[size];
        }
    };

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public String getRegisteredApplink() {
        return registeredApplink;
    }

    public void setRegisteredApplink(String registeredApplink) {
        this.registeredApplink = registeredApplink;
    }

    public String getNotRegisteredApplink() {
        return notRegisteredApplink;
    }

    public void setNotRegisteredApplink(String notRegisteredApplink) {
        this.notRegisteredApplink = notRegisteredApplink;
    }

    public String getChangeMsisdnApplink() {
        return changeMsisdnApplink;
    }

    public void setChangeMsisdnApplink(String changeMsisdnApplink) {
        this.changeMsisdnApplink = changeMsisdnApplink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(phoneNumber);
        parcel.writeByte((byte) (isRegistered ? 1 : 0));
        parcel.writeString(registeredApplink);
        parcel.writeString(notRegisteredApplink);
        parcel.writeString(changeMsisdnApplink);
    }
}
