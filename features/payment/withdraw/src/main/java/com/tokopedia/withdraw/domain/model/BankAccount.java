
package com.tokopedia.withdraw.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

public class BankAccount implements Parcelable {

    @SerializedName("bank_id")
    @Expose
    private String bankId;
    @SerializedName("acc_id")
    @Expose
    private String bankAccountId;
    @SerializedName("acc_name")
    @Expose
    private String bankAccountName;
    @SerializedName("branch")
    @Expose
    private String bankBranch;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("acc_number")
    @Expose
    private String bankAccountNumber;

    @SerializedName("type")
    @Expose
    private String type;

    private boolean isChecked;

    /**
     * @return The bankId
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * @param bankId The bank_id
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    /**
     * @return The bankBranch
     */
    public String getBankBranch() {
        return bankBranch;
    }

    /**
     * @param bankBranch The bank_branch
     */
    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    /**
     * @return The bankAccountName
     */
    public String getBankAccountName() {
        return MethodChecker.fromHtml(bankAccountName).toString();
    }

    /**
     * @param bankAccountName The bank_account_name
     */
    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    /**
     * @return The bankAccountNumber
     */
    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    /**
     * @param bankAccountNumber The bank_account_number
     */
    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    /**
     * @return The bankAccountId
     */
    public String getBankAccountId() {
        return bankAccountId;
    }

    /**
     * @param bankAccountId The bank_account_id
     */
    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    /**
     * @return The bankName
     */
    public String getBankName() {
        return MethodChecker.fromHtml(bankName).toString();
    }

    /**
     * @param bankName The bank_name
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setChecked(boolean b) {
        this.isChecked = b;
    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bankId);
        dest.writeString(this.bankBranch);
        dest.writeString(this.bankAccountName);
        dest.writeString(this.bankAccountNumber);
        dest.writeString(this.bankAccountId);
        dest.writeString(this.bankName);
        dest.writeString(this.type);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    public BankAccount() {
    }

    protected BankAccount(Parcel in) {
        this.bankId = in.readString();
        this.bankBranch = in.readString();
        this.bankAccountName = in.readString();
        this.bankAccountNumber = in.readString();
        this.bankAccountId = in.readString();
        this.bankName = in.readString();
        this.type = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    public static final Creator<BankAccount> CREATOR = new Creator<BankAccount>() {
        @Override
        public BankAccount createFromParcel(Parcel source) {
            return new BankAccount(source);
        }

        @Override
        public BankAccount[] newArray(int size) {
            return new BankAccount[size];
        }
    };
}
