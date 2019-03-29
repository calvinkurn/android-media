package com.tokopedia.core.session.model;


/**
 * Created by m.normansyah on 17/11/2015.
 */
public class LoginFacebookViewModel {
    String fullName;
    String gender;
    String birthday;
    String fbToken;
    String fbId;
    String email;
    String education;
    String interest;
    String work;
    String uuid;

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "LoginFacebookViewModel{" +
                "fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", fbToken='" + fbToken + '\'' +
                ", fbId='" + fbId + '\'' +
                ", email='" + email + '\'' +
                ", education='" + education + '\'' +
                ", interest='" + interest + '\'' +
                ", work='" + work + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
