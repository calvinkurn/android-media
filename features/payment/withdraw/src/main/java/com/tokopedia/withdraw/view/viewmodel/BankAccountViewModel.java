//
//package com.tokopedia.withdraw.view.viewmodel;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.tokopedia.abstraction.common.utils.view.MethodChecker;
//
//public class BankAccountViewModel implements Parcelable{
//
//    private int bankId;
//    private int bankBranch;
//    private String bankAccountName;
//    private String bankAccountNumber;
//    private int isVerifiedAccount;
//    private String bankAccountId;
//    private String bankName;
//    private int isDefaultBank;
//    private boolean checked;
//
//    public BankAccountViewModel() {
//        this.checked = false;
//    }
//
//    public int getBankId() {
//        return bankId;
//    }
//
//    public void setBankId(int bankId) {
//        this.bankId = bankId;
//    }
//
//    public int getBankBranch() {
//        return bankBranch;
//    }
//
//    public void setBankBranch(int bankBranch) {
//        this.bankBranch = bankBranch;
//    }
//
//    public String getBankAccountName() {
//        return MethodChecker.fromHtml(bankAccountName).toString();
//    }
//
//    public void setBankAccountName(String bankAccountName) {
//        this.bankAccountName = bankAccountName;
//    }
//
//    public String getBankAccountNumber() {
//        return bankAccountNumber;
//    }
//
//    public void setBankAccountNumber(String bankAccountNumber) {
//        this.bankAccountNumber = bankAccountNumber;
//    }
//
//    public Boolean isVerifiedAccount() {
//        return isVerifiedAccount == 1;
//    }
//
//    public void setIsVerifiedAccount(int isVerifiedAccount) {
//        this.isVerifiedAccount = isVerifiedAccount;
//    }
//
//    public String getBankAccountId() {
//        return bankAccountId;
//    }
//
//    public void setBankAccountId(String bankAccountId) {
//        this.bankAccountId = bankAccountId;
//    }
//
//    public String getBankName() {
//        return MethodChecker.fromHtml(bankName).toString();
//    }
//
//    public void setBankName(String bankName) {
//        this.bankName = bankName;
//    }
//
//    public int getIsDefaultBank() {
//        return isDefaultBank;
//    }
//
//    public void setIsDefaultBank(int isDefaultBank) {
//        this.isDefaultBank = isDefaultBank;
//    }
//
//    public boolean isChecked() {
//        return checked;
//    }
//
//    public void setChecked(boolean checked) {
//        this.checked = checked;
//    }
//
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.bankId);
//        dest.writeInt(this.bankBranch);
//        dest.writeString(this.bankAccountName);
//        dest.writeString(this.bankAccountNumber);
//        dest.writeInt(this.isVerifiedAccount);
//        dest.writeString(this.bankAccountId);
//        dest.writeString(this.bankName);
//        dest.writeInt(this.isDefaultBank);
//        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
//    }
//
//    protected BankAccountViewModel(Parcel in) {
//        this.bankId = in.readInt();
//        this.bankBranch = in.readInt();
//        this.bankAccountName = in.readString();
//        this.bankAccountNumber = in.readString();
//        this.isVerifiedAccount = in.readInt();
//        this.bankAccountId = in.readString();
//        this.bankName = in.readString();
//        this.isDefaultBank = in.readInt();
//        this.checked = in.readByte() != 0;
//    }
//
//    public static final Creator<BankAccountViewModel> CREATOR = new Creator<BankAccountViewModel>() {
//        @Override
//        public BankAccountViewModel createFromParcel(Parcel source) {
//            return new BankAccountViewModel(source);
//        }
//
//        @Override
//        public BankAccountViewModel[] newArray(int size) {
//            return new BankAccountViewModel[size];
//        }
//    };
//}
