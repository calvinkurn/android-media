package com.tokopedia.core.session.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by m.normansyah on 17/11/2015.
 */
@Parcel
public class LoginGoogleModel {
    String fullName;
    String googleId;
    String email;
    String gender;
    String birthday;
    String imageUrl;
    String uuid;

    public LoginGoogleModel() {
    }

    @ParcelConstructor
    public LoginGoogleModel(String fullName, String googleId, String email, String gender,
                            String birthday, String imageUrl) {
        this.fullName = fullName;
        this.googleId = googleId;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "LoginGoogleModel{" +
                "fullName='" + fullName + '\'' +
                ", googleId='" + googleId + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
