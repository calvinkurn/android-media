package com.tokopedia.posapp.payment.cardscanner.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by okasurya on 10/6/17.
 */

public class PaymentViewModel implements Parcelable {
    private int bankId;
    private String bankName;
    private List<String> validateBin;
    private List<String> installmentBin;
    private Boolean allowInstallment;
    private Integer emiId;
    private double paymentAmount;
    private String merchantCode;
    private String profileCode;
    private int transactionId;
    private String signature;
    private CreditCardViewModel creditCard;

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<String> getValidateBin() {
        return validateBin;
    }

    public void setValidateBin(List<String> validateBin) {
        this.validateBin = validateBin;
    }

    public List<String> getInstallmentBin() {
        return installmentBin;
    }

    public void setInstallmentBin(List<String> installmentBin) {
        this.installmentBin = installmentBin;
    }

    public Boolean getAllowInstallment() {
        return allowInstallment;
    }

    public void setAllowInstallment(Boolean allowInstallment) {
        this.allowInstallment = allowInstallment;
    }

    public Integer getEmiId() {
        return emiId;
    }

    public void setEmiId(Integer emiId) {
        this.emiId = emiId;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public CreditCardViewModel getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardViewModel creditCard) {
        this.creditCard = creditCard;
    }


    public PaymentViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bankId);
        dest.writeString(this.bankName);
        dest.writeStringList(this.validateBin);
        dest.writeStringList(this.installmentBin);
        dest.writeValue(this.allowInstallment);
        dest.writeValue(this.emiId);
        dest.writeDouble(this.paymentAmount);
        dest.writeString(this.merchantCode);
        dest.writeString(this.profileCode);
        dest.writeInt(this.transactionId);
        dest.writeString(this.signature);
        dest.writeParcelable(this.creditCard, flags);
    }

    protected PaymentViewModel(Parcel in) {
        this.bankId = in.readInt();
        this.bankName = in.readString();
        this.validateBin = in.createStringArrayList();
        this.installmentBin = in.createStringArrayList();
        this.allowInstallment = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.emiId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.paymentAmount = in.readDouble();
        this.merchantCode = in.readString();
        this.profileCode = in.readString();
        this.transactionId = in.readInt();
        this.signature = in.readString();
        this.creditCard = in.readParcelable(CreditCardViewModel.class.getClassLoader());
    }

    public static final Creator<PaymentViewModel> CREATOR = new Creator<PaymentViewModel>() {
        @Override
        public PaymentViewModel createFromParcel(Parcel source) {
            return new PaymentViewModel(source);
        }

        @Override
        public PaymentViewModel[] newArray(int size) {
            return new PaymentViewModel[size];
        }
    };
}
