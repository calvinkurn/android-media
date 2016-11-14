package com.tokopedia.core.manage.people.bank.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nisie on 6/13/16.
 */
public class ActSettingBankPass implements Parcelable{

    private static final String PARAM_ACCOUNT_ID = "account_id";
    private static final String PARAM_ACCOUNT_NAME = "account_name";
    private static final String PARAM_ACCOUNT_NUMBER = "account_no";
    private static final String PARAM_BANK_BRANCH = "bank_branch";
    private static final String PARAM_BANK_ID = "bank_id";
    private static final String PARAM_BANK_NAME = "bank_name";
    private static final String PARAM_OTP_CODE = "otp_code";
    private static final String PARAM_USER_PASSWORD = "user_password";
    private static final String PARAM_OWNER_ID = "owner_id";

    String accountId;
    String accountName;
    String accountNumber;
    String bankBranch;
    String bankId;
    String bankName;
    String otpCode;
    String userPassword;
    int position;
    String ownerId;

    public ActSettingBankPass() {

    }

    protected ActSettingBankPass(Parcel in) {
        accountId = in.readString();
        accountName = in.readString();
        accountNumber = in.readString();
        bankBranch = in.readString();
        bankId = in.readString();
        bankName = in.readString();
        otpCode = in.readString();
        userPassword = in.readString();
        position = in.readInt();
        ownerId = in.readString();


    }

    public static final Creator<ActSettingBankPass> CREATOR = new Creator<ActSettingBankPass>() {
        @Override
        public ActSettingBankPass createFromParcel(Parcel in) {
            return new ActSettingBankPass(in);
        }

        @Override
        public ActSettingBankPass[] newArray(int size) {
            return new ActSettingBankPass[size];
        }
    };

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Map<String, String> getParamAddBankAccount() {
        Map<String, String> param = new HashMap<>();
        param.put(PARAM_ACCOUNT_NAME, getAccountName());
        param.put(PARAM_ACCOUNT_NUMBER, getAccountNumber());
        param.put(PARAM_BANK_BRANCH, getBankBranch());
        param.put(PARAM_BANK_ID, getBankId());
        param.put(PARAM_BANK_NAME, getBankName());
        param.put(PARAM_USER_PASSWORD, getUserPassword());
        param.put(PARAM_OTP_CODE, getOtpCode());
        return param;
    }

    public Map<String, String> getParamEditBankAccount() {
        Map<String, String> param = new HashMap<>();
        param.put(PARAM_ACCOUNT_ID, getAccountId());
        param.put(PARAM_ACCOUNT_NAME, getAccountName());
        param.put(PARAM_ACCOUNT_NUMBER, getAccountNumber());
        param.put(PARAM_BANK_BRANCH, getBankBranch());
        param.put(PARAM_BANK_ID, getBankId());
        param.put(PARAM_BANK_NAME, getBankName());
        param.put(PARAM_USER_PASSWORD, getUserPassword());
        param.put(PARAM_OTP_CODE, getOtpCode());
        return param;
    }

    public Map<String, String> getParamDeleteBankAccount() {
        Map<String, String> param = new HashMap<>();
        param.put(PARAM_ACCOUNT_ID, getAccountId());
        return param;
    }

    public Map<String, String> getParamDefaultBankAccount() {
        Map<String, String> param = new HashMap<>();
        param.put(PARAM_ACCOUNT_ID, getAccountId());
        param.put(PARAM_OWNER_ID, getOwnerId());
        return param;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountId);
        dest.writeString(accountName);
        dest.writeString(accountNumber);
        dest.writeString(bankBranch);
        dest.writeString(bankId);
        dest.writeString(bankName);
        dest.writeString(otpCode);
        dest.writeString(userPassword);
        dest.writeInt(position);
        dest.writeString(ownerId);
    }
}
