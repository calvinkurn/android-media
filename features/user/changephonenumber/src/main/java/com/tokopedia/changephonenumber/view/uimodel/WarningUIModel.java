package com.tokopedia.changephonenumber.view.uimodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by milhamj on 19/12/17.
 */

public class WarningUIModel implements Parcelable {
    public static final String EMPTY_BALANCE = "Rp  0";
    public static final int BALANCE_THRESHOLD_FOR_WARNING = 15000;
    public static final String ACTION_EMAIL = "send_email";
    public static final String ACTION_OTP = "send_otp";

    private String tokopediaBalance;
    private String tokocash;
    private long tokopediaBalanceNumber;
    private long tokocashNumber;
    private String action;
    private List<String> warningList;
    private boolean hasBankAccount;
    private boolean isOtpValidate;
    private boolean isOvoEligible;
    private String urlOvo;

    public WarningUIModel() {
    }

    public String getTokopediaBalance() {
        return tokopediaBalance;
    }

    public void setTokopediaBalance(String tokopediaBalance) {
        this.tokopediaBalance = tokopediaBalance;
    }

    public String getTokocash() {
        return tokocash;
    }

    public void setTokocash(String tokocash) {
        this.tokocash = tokocash;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getWarningList() {
        return warningList;
    }

    public void setWarningList(List<String> warningList) {
        this.warningList = warningList;
    }

    public boolean isHasBankAccount() {
        return hasBankAccount;
    }

    public void setHasBankAccount(boolean hasBankAccount) {
        this.hasBankAccount = hasBankAccount;
    }

    public long getTokopediaBalanceNumber() {
        return tokopediaBalanceNumber;
    }

    public void setTokopediaBalanceNumber(long tokopediaBalanceNumber) {
        this.tokopediaBalanceNumber = tokopediaBalanceNumber;
    }

    public long getTokocashNumber() {
        return tokocashNumber;
    }

    public void setTokocashNumber(long tokocashNumber) {
        this.tokocashNumber = tokocashNumber;
    }

    public boolean isOtpValidate() {
        return isOtpValidate;
    }

    public void setOtpValidate(boolean otpValidate) {
        isOtpValidate = otpValidate;
    }

    public boolean isOvoEligible() {
        return isOvoEligible;
    }

    public void setOvoEligible(boolean ovoEligible) {
        isOvoEligible = ovoEligible;
    }
    public String getUrlOvo() {
        return urlOvo;
    }

    public void setUrlOvo(String urlOvo) {
        this.urlOvo = urlOvo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokopediaBalance);
        dest.writeString(this.tokocash);
        dest.writeLong(this.tokopediaBalanceNumber);
        dest.writeLong(this.tokocashNumber);
        dest.writeString(this.action);
        dest.writeStringList(this.warningList);
        dest.writeByte(this.hasBankAccount ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isOtpValidate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isOvoEligible ? (byte) 1 : (byte) 0);
        dest.writeString(this.urlOvo);
    }

    protected WarningUIModel(Parcel in) {
        this.tokopediaBalance = in.readString();
        this.tokocash = in.readString();
        this.tokopediaBalanceNumber = in.readLong();
        this.tokocashNumber = in.readLong();
        this.action = in.readString();
        this.warningList = in.createStringArrayList();
        this.hasBankAccount = in.readByte() != 0;
        this.isOtpValidate = in.readByte() != 0;
        this.isOvoEligible = in.readByte() != 0;
        this.urlOvo = in.readString();
    }

    public static final Creator<WarningUIModel> CREATOR = new Creator<WarningUIModel>() {
        @Override
        public WarningUIModel createFromParcel(Parcel source) {
            return new WarningUIModel(source);
        }

        @Override
        public WarningUIModel[] newArray(int size) {
            return new WarningUIModel[size];
        }
    };
}
