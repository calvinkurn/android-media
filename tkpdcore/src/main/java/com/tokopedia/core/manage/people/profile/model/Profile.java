package com.tokopedia.core.manage.people.profile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile implements Parcelable {

    @SerializedName("data_user")
    @Expose
    private DataUser dataUser;
    @SerializedName("is_success")
    @Expose
    private String isSuccess;

    /**
     *
     * @return
     *     The dataUser
     */
    public DataUser getDataUser() {
        return dataUser;
    }

    /**
     *
     * @param dataUser
     *     The data_user
     */
    public void setDataUser(DataUser dataUser) {
        this.dataUser = dataUser;
    }

    /**
     *
     * @return
     *     The isSuccess
     */
    public String getIsSuccess() {
        return isSuccess;
    }

    /**
     *
     * @param isSuccess
     *     The is_success
     */
    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     *
     * @return
     *     The success
     */
    public Boolean isSuccess() {
        return isSuccess.equals("1");
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.dataUser, flags);
        dest.writeString(this.isSuccess);
    }

    public Profile() {
    }

    protected Profile(Parcel in) {
        this.dataUser = in.readParcelable(DataUser.class.getClassLoader());
        this.isSuccess = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel source) {
            return new Profile(source);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
}