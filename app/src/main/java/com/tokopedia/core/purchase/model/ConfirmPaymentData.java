package com.tokopedia.core.purchase.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Angga.Prasetiyo
 * on 23/06/2016.
 */
public class ConfirmPaymentData implements Parcelable {

    private String paymentId;
    private String comments;
    private String depositor;
    private String paymentAmount = "";
    private String paymentDay;
    private String paymentMethod;
    private String paymentMonth;
    private String paymentYear;
    private String token;
    private String bankAccountId;
    private String bankAccountName;
    private String bankAccountNumber;
    private String bankId;
    private String bankName;
    private String bankAccountBranch;
    private String passwordDeposit;
    private String sysBankId;

    private boolean isConfirmation;
    private boolean isNewAccountBank;

    public ConfirmPaymentData() {
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDepositor() {
        return depositor;
    }

    public void setDepositor(String depositor) {
        this.depositor = depositor;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDay() {
        return paymentDay;
    }

    public void setPaymentDay(String paymentDay) {
        this.paymentDay = paymentDay;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(String paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public String getPaymentYear() {
        return paymentYear;
    }

    public void setPaymentYear(String paymentYear) {
        this.paymentYear = paymentYear;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
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

    public String getBankAccountBranch() {
        return bankAccountBranch;
    }

    public void setBankAccountBranch(String bankAccountBranch) {
        this.bankAccountBranch = bankAccountBranch;
    }

    public String getPasswordDeposit() {
        return passwordDeposit;
    }

    public void setPasswordDeposit(String passwordDeposit) {
        this.passwordDeposit = passwordDeposit;
    }

    public String getSysBankId() {
        return sysBankId;
    }

    public void setSysBankId(String sysBankId) {
        this.sysBankId = sysBankId;
    }

    public boolean isConfirmation() {
        return isConfirmation;
    }

    public void setConfirmation(boolean confirmation) {
        isConfirmation = confirmation;
    }

    public boolean isNewAccountBank() {
        return isNewAccountBank;
    }

    public void setNewAccountBank(boolean newAccountBank) {
        isNewAccountBank = newAccountBank;
    }

    protected ConfirmPaymentData(Parcel in) {
        paymentId = in.readString();
        comments = in.readString();
        depositor = in.readString();
        paymentAmount = in.readString();
        paymentDay = in.readString();
        paymentMethod = in.readString();
        paymentMonth = in.readString();
        paymentYear = in.readString();
        token = in.readString();
        bankAccountId = in.readString();
        bankAccountName = in.readString();
        bankAccountNumber = in.readString();
        bankId = in.readString();
        bankName = in.readString();
        bankAccountBranch = in.readString();
        passwordDeposit = in.readString();
        sysBankId = in.readString();
        isConfirmation = in.readByte() != 0x00;
        isNewAccountBank = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paymentId);
        dest.writeString(comments);
        dest.writeString(depositor);
        dest.writeString(paymentAmount);
        dest.writeString(paymentDay);
        dest.writeString(paymentMethod);
        dest.writeString(paymentMonth);
        dest.writeString(paymentYear);
        dest.writeString(token);
        dest.writeString(bankAccountId);
        dest.writeString(bankAccountName);
        dest.writeString(bankAccountNumber);
        dest.writeString(bankId);
        dest.writeString(bankName);
        dest.writeString(bankAccountBranch);
        dest.writeString(passwordDeposit);
        dest.writeString(sysBankId);
        dest.writeByte((byte) (isConfirmation ? 0x01 : 0x00));
        dest.writeByte((byte) (isNewAccountBank ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ConfirmPaymentData> CREATOR =
            new Parcelable.Creator<ConfirmPaymentData>() {
                @Override
                public ConfirmPaymentData createFromParcel(Parcel in) {
                    return new ConfirmPaymentData(in);
                }

                @Override
                public ConfirmPaymentData[] newArray(int size) {
                    return new ConfirmPaymentData[size];
                }
            };
}
