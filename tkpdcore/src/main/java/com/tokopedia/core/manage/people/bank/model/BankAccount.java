package com.tokopedia.core.manage.people.bank.model;

import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

/**
 * Created by Nisie on 6/10/16.
 */

public class BankAccount {

    @SerializedName("bank_id")
    @Expose
    private int bankId;
    @SerializedName("bank_branch")
    @Expose
    private String bankBranch;
    @SerializedName("bank_account_name")
    @Expose
    private String bankAccountName;
    @SerializedName("bank_account_number")
    @Expose
    private String bankAccountNumber;
    @SerializedName("is_verified_account")
    @Expose
    private int isVerifiedAccount;
    @SerializedName("bank_account_id")
    @Expose
    private String bankAccountId;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("is_default_bank")
    @Expose
    private int isDefaultBank;
    @SerializedName("bank_logo")
    @Expose
    private String bankLogo;

    /**
     * @return The bankId
     */
    public int getBankId() {
        return bankId;
    }

    /**
     * @param bankId The bank_id
     */
    public void setBankId(int bankId) {
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

    /**n
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
     * @return The isVerifiedAccount
     */
    public Boolean isVerifiedAccount() {
        return isVerifiedAccount == 1;
    }

    /**
     * @param isVerifiedAccount The is_verified_account
     */
    public void setIsVerifiedAccount(int isVerifiedAccount) {
        this.isVerifiedAccount = isVerifiedAccount;
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

    /**
     * @return The isDefaultBank
     */
    public int getIsDefaultBank() {
        return isDefaultBank;
    }

    /**
     * @param isDefaultBank The is_default_bank
     */
    public void setIsDefaultBank(int isDefaultBank) {
        this.isDefaultBank = isDefaultBank;
    }

    /**
     * @return The bankLogo
     */
    public String getBankLogo() {
        return bankLogo != null ? bankLogo : "";
    }

    /**
     * @param bankLogo The bank_logo
     */
    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }
}
