package com.tokopedia.core.manage.people.profile.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by stevenfredian on 4/29/16.
 */
public class PeopleProfilePass implements Parcelable {

    private String imagePath;
    private String serverID;
    private String uploadHost;
    private String day;
    private String month;
    private String year;
    private String email;
    private String fullName;
    private String gender;
    private String hobby;
    private String messenger;
    private String msisdn;
    private String verifiedPhone;
    private String userID;
    private String filePath;
    private boolean success;
    private String fileUploaded;
    private boolean byPass;

    public PeopleProfilePass() {

    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getVerifiedPhone() {
        return verifiedPhone;
    }

    public void setVerifiedPhone(String verifiedPhone) {
        this.verifiedPhone = verifiedPhone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getUploadHost() {
        return uploadHost;
    }

    public void setUploadHost(String uploadHost) {
        this.uploadHost = uploadHost;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(String fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public void setByPass(boolean byPass) {
        this.byPass = byPass;
    }

    public boolean isByPass() {
        return byPass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imagePath);
        dest.writeString(this.serverID);
        dest.writeString(this.uploadHost);
        dest.writeString(this.day);
        dest.writeString(this.month);
        dest.writeString(this.year);
        dest.writeString(this.email);
        dest.writeString(this.fullName);
        dest.writeString(this.gender);
        dest.writeString(this.hobby);
        dest.writeString(this.messenger);
        dest.writeString(this.msisdn);
        dest.writeString(this.verifiedPhone);
        dest.writeString(this.userID);
        dest.writeString(this.filePath);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.fileUploaded);
        dest.writeByte(this.byPass ? (byte) 1 : (byte) 0);
    }

    protected PeopleProfilePass(Parcel in) {
        this.imagePath = in.readString();
        this.serverID = in.readString();
        this.uploadHost = in.readString();
        this.day = in.readString();
        this.month = in.readString();
        this.year = in.readString();
        this.email = in.readString();
        this.fullName = in.readString();
        this.gender = in.readString();
        this.hobby = in.readString();
        this.messenger = in.readString();
        this.msisdn = in.readString();
        this.verifiedPhone = in.readString();
        this.userID = in.readString();
        this.filePath = in.readString();
        this.success = in.readByte() != 0;
        this.fileUploaded = in.readString();
        this.byPass = in.readByte() != 0;
    }

    public static final Creator<PeopleProfilePass> CREATOR = new Creator<PeopleProfilePass>() {
        @Override
        public PeopleProfilePass createFromParcel(Parcel source) {
            return new PeopleProfilePass(source);
        }

        @Override
        public PeopleProfilePass[] newArray(int size) {
            return new PeopleProfilePass[size];
        }
    };
}