
package com.tokopedia.core.deposit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WithdrawForm {

    @SerializedName("msisdn_verified")
    @Expose
    private int msisdnVerified;
    @SerializedName("useable_deposit")
    @Expose
    private int useableDeposit;
    @SerializedName("bank_account")
    @Expose
    private List<BankAccount> bankAccount = new ArrayList<BankAccount>();
    @SerializedName("useable_deposit_idr")
    @Expose
    private String useableDepositIdr;

    /**
     * 
     * @return
     *     The msisdnVerified
     */
    public int isMsisdnVerified() {
        return msisdnVerified;
    }

    /**
     * 
     * @param msisdnVerified
     *     The msisdn_verified
     */
    public void setMsisdnVerified(int msisdnVerified) {
        this.msisdnVerified = msisdnVerified;
    }

    /**
     * 
     * @return
     *     The useableDeposit
     */
    public int getUseableDeposit() {
        return useableDeposit;
    }

    /**
     * 
     * @param useableDeposit
     *     The useable_deposit
     */
    public void setUseableDeposit(int useableDeposit) {
        this.useableDeposit = useableDeposit;
    }

    /**
     * 
     * @return
     *     The bankAccount
     */
    public List<BankAccount> getBankAccount() {
        return bankAccount;
    }

    /**
     * 
     * @param bankAccount
     *     The bank_account
     */
    public void setBankAccount(List<BankAccount> bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * 
     * @return
     *     The useableDepositIdr
     */
    public String getUseableDepositIdr() {
        return useableDepositIdr;
    }

    /**
     * 
     * @param useableDepositIdr
     *     The useable_deposit_idr
     */
    public void setUseableDepositIdr(String useableDepositIdr) {
        this.useableDepositIdr = useableDepositIdr;
    }

}
