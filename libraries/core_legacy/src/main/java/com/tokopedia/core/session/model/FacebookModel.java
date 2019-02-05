package com.tokopedia.core.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FacebookModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("email")
    @Expose
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getBirthdayConverted(){
        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(birthday);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date != null) return  (outputFormat.format(date));

        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.email);
    }

    public FacebookModel() {
    }

    protected FacebookModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.gender = in.readString();
        this.birthday = in.readString();
        this.email = in.readString();
    }

    public static final Creator<FacebookModel> CREATOR = new Creator<FacebookModel>() {
        @Override
        public FacebookModel createFromParcel(Parcel source) {
            return new FacebookModel(source);
        }

        @Override
        public FacebookModel[] newArray(int size) {
            return new FacebookModel[size];
        }
    };
}
