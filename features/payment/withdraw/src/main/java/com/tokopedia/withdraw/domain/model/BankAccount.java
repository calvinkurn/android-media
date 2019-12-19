
package com.tokopedia.withdraw.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

public class BankAccount implements Parcelable {

    @SerializedName("bankID")
    @Expose
    private long bankID;
    @SerializedName("accountNo")
    @Expose
    private String accountNo;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("bankAccountID")
    @Expose
    private long bankAccountID;
    @SerializedName("minAmount")
    @Expose
    private long minAmount;
    @SerializedName("maxAmount")
    @Expose
    private long maxAmount;
    @SerializedName("adminFee")
    @Expose
    private long adminFee;
    @SerializedName("status")
    @Expose
    private long status;
    @SerializedName("isVerifiedAccount")
    @Expose
    private long isVerifiedAccount;
    @SerializedName("bankImageUrl")
    @Expose
    private String bankImageUrl;
    @SerializedName("isDefaultBank")
    @Expose
    private int isDefaultBank;
    @SerializedName("accountName")
    @Expose
    private String accountName;

    private boolean isChecked;

    public long getBankID() {
        return bankID;
    }

    public void setBankID(long bankID) {
        this.bankID = bankID;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public long getBankAccountID() {
        return bankAccountID;
    }

    public void setBankAccountID(long bankAccountID) {
        this.bankAccountID = bankAccountID;
    }

    public long getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(long minAmount) {
        this.minAmount = minAmount;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(long maxAmount) {
        this.maxAmount = maxAmount;
    }

    public long getAdminFee() {
        return adminFee;
    }

    public void setAdminFee(long adminFee) {
        this.adminFee = adminFee;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getIsVerifiedAccount() {
        return isVerifiedAccount;
    }

    public void setIsVerifiedAccount(long isVerifiedAccount) {
        this.isVerifiedAccount = isVerifiedAccount;
    }

    public String getBankImageUrl() {
        return bankImageUrl;
    }

    public void setBankImageUrl(String bankImageUrl) {
        this.bankImageUrl = bankImageUrl;
    }

    public int getIsDefaultBank() {
        return isDefaultBank;
    }

    public void setIsDefaultBank(int isDefaultBank) {
        this.isDefaultBank = isDefaultBank;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public BankAccount() {
    }

    protected BankAccount(Parcel in) {
        bankID = in.readLong();
        accountNo = in.readString();
        bankName = in.readString();
        bankAccountID = in.readLong();
        minAmount = in.readLong();
        maxAmount = in.readLong();
        adminFee = in.readLong();
        status = in.readLong();
        isVerifiedAccount = in.readLong();
        bankImageUrl = in.readString();
        isDefaultBank = in.readInt();
        accountName = in.readString();
    }

    public static final Creator<BankAccount> CREATOR = new Creator<BankAccount>() {
        @Override
        public BankAccount createFromParcel(Parcel in) {
            return new BankAccount(in);
        }

        @Override
        public BankAccount[] newArray(int size) {
            return new BankAccount[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(bankID);
        dest.writeString(accountNo);
        dest.writeString(bankName);
        dest.writeLong(bankAccountID);
        dest.writeLong(minAmount);
        dest.writeLong(maxAmount);
        dest.writeLong(adminFee);
        dest.writeLong(status);
        dest.writeLong(isVerifiedAccount);
        dest.writeString(bankImageUrl);
        dest.writeInt(isDefaultBank);
        dest.writeString(accountName);
    }
}
