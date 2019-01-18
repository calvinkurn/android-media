package com.tokopedia.sessioncommon.data.loginphone;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.List;

/**
 * @author by nisie on 12/5/17.
 */

public class ChooseTokoCashAccountViewModel implements Parcelable {

    public static final String ARGS_DATA = "data";

    private List<UserDetail> listAccount;
    private String phoneNumber;
    private String accessToken;

    public ChooseTokoCashAccountViewModel(List<UserDetail> listAccount,
                                          String phoneNumber,
                                          String accessToken) {
        this.listAccount = listAccount;
        this.phoneNumber = phoneNumber;
        this.accessToken = accessToken;
    }

    protected ChooseTokoCashAccountViewModel(Parcel in) {
        listAccount = in.createTypedArrayList(UserDetail.CREATOR);
        phoneNumber = in.readString();
        accessToken = in.readString();
    }

    public static final Creator<ChooseTokoCashAccountViewModel> CREATOR = new Creator<ChooseTokoCashAccountViewModel>() {
        @Override
        public ChooseTokoCashAccountViewModel createFromParcel(Parcel in) {
            return new ChooseTokoCashAccountViewModel(in);
        }

        @Override
        public ChooseTokoCashAccountViewModel[] newArray(int size) {
            return new ChooseTokoCashAccountViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listAccount);
        dest.writeString(phoneNumber);
        dest.writeString(accessToken);
    }

    public List<UserDetail> getListAccount() {
        return listAccount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getKey() {
        return accessToken;
    }
}
