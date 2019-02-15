//package com.tokopedia.withdraw.domain.model;
//
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//import com.tokopedia.withdraw.view.model.BankAccount;
//
//import java.util.List;
//
//public class InfoDepositPojo {
//
//    @SerializedName("useable_deposit")
//    @Expose
//    private int useableDeposit;
//    @SerializedName("useable_deposit_idr")
//    @Expose
//    private String useableDepositIdr;
//    @SerializedName("msisdn_verified")
//    @Expose
//    private int msisdnVerified;
//    @SerializedName("bank_account")
//    @Expose
//    private List<BankAccount> bankAccount = null;
//
//    public int getUseableDeposit() {
//        return useableDeposit;
//    }
//
//    public void setUseableDeposit(int useableDeposit) {
//        this.useableDeposit = useableDeposit;
//    }
//
//    public String getUseableDepositIdr() {
//        return useableDepositIdr;
//    }
//
//    public void setUseableDepositIdr(String useableDepositIdr) {
//        this.useableDepositIdr = useableDepositIdr;
//    }
//
//    public int getMsisdnVerified() {
//        return msisdnVerified;
//    }
//
//    public void setMsisdnVerified(int msisdnVerified) {
//        this.msisdnVerified = msisdnVerified;
//    }
//
//    public List<BankAccount> getBankAccount() {
//        return bankAccount;
//    }
//
//    public void setBankAccount(List<BankAccount> bankAccount) {
//        this.bankAccount = bankAccount;
//    }
//
//}