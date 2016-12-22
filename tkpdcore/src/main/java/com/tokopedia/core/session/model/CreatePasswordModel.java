package com.tokopedia.core.session.model;

import org.parceler.Parcel;

/**
 * Created by Nisie on 3/28/16.
 */
@Parcel
public class CreatePasswordModel {

    String email;
    int bdayDay;
    int bdayMonth;
    int bdayYear;
    String fullName;
    String gender;
    String msisdn;
    String newPass;
    String confirmPass;
    String registerTos;
    String dateText;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBdayDay() {
        return bdayDay;
    }

    public void setBdayDay(int bdayDay) {
        this.bdayDay = bdayDay;
    }

    public int getBdayMonth() {
        return bdayMonth;
    }

    public void setBdayMonth(int bdayMonth) {
        this.bdayMonth = bdayMonth;
    }

    public int getBdayYear() {
        return bdayYear;
    }

    public void setBdayYear(int bdayYear) {
        this.bdayYear = bdayYear;
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

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

    public String getRegisterTos() {
        return registerTos;
    }

    public void setRegisterTos(String registerTos) {
        this.registerTos = registerTos;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }
}
