
package com.tokopedia.tkpd.deposit.model;

import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deposit {

    @SerializedName("deposit_id")
    @Expose
    private String depositId;
    @SerializedName("deposit_saldo_idr")
    @Expose
    private String depositSaldoIdr;
    @SerializedName("deposit_date_full")
    @Expose
    private String depositDateFull;
    @SerializedName("deposit_amount")
    @Expose
    private String depositAmount;
    @SerializedName("deposit_amount_idr")
    @Expose
    private String depositAmountIdr;
    @SerializedName("deposit_type")
    @Expose
    private int depositType;
    @SerializedName("deposit_date")
    @Expose
    private String depositDate;
    @SerializedName("deposit_withdraw_date")
    @Expose
    private String depositWithdrawDate;
    @SerializedName("deposit_withdraw_status")
    @Expose
    private String depositWithdrawStatus;
    @SerializedName("deposit_notes")
    @Expose
    private String depositNotes;

    /**
     * 
     * @return
     *     The depositId
     */
    public String getDepositId() {
        return depositId;
    }

    /**
     * 
     * @param depositId
     *     The deposit_id
     */
    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    /**
     * 
     * @return
     *     The depositSaldoIdr
     */
    public String getDepositSaldoIdr() {
        return depositSaldoIdr;
    }

    /**
     * 
     * @param depositSaldoIdr
     *     The deposit_saldo_idr
     */
    public void setDepositSaldoIdr(String depositSaldoIdr) {
        this.depositSaldoIdr = depositSaldoIdr;
    }

    /**
     * 
     * @return
     *     The depositDateFull
     */
    public String getDepositDateFull() {
        return depositDateFull;
    }

    /**
     * 
     * @param depositDateFull
     *     The deposit_date_full
     */
    public void setDepositDateFull(String depositDateFull) {
        this.depositDateFull = depositDateFull;
    }

    /**
     * 
     * @return
     *     The depositAmount
     */
    public String getDepositAmount() {
        return depositAmount;
    }

    /**
     * 
     * @param depositAmount
     *     The deposit_amount
     */
    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    /**
     * 
     * @return
     *     The depositAmountIdr
     */
    public String getDepositAmountIdr() {
        return depositAmountIdr;
    }

    /**
     * 
     * @param depositAmountIdr
     *     The deposit_amount_idr
     */
    public void setDepositAmountIdr(String depositAmountIdr) {
        this.depositAmountIdr = depositAmountIdr;
    }

    /**
     * 
     * @return
     *     The depositType
     */
    public int getDepositType() {
        return depositType;
    }

    /**
     * 
     * @param depositType
     *     The deposit_type
     */
    public void setDepositType(int depositType) {
        this.depositType = depositType;
    }

    /**
     * 
     * @return
     *     The depositDate
     */
    public String getDepositDate() {
        return depositDate;
    }

    /**
     * 
     * @param depositDate
     *     The deposit_date
     */
    public void setDepositDate(String depositDate) {
        this.depositDate = depositDate;
    }

    /**
     * 
     * @return
     *     The depositWithdrawDate
     */
    public String getDepositWithdrawDate() {
        return depositWithdrawDate;
    }

    /**
     * 
     * @param depositWithdrawDate
     *     The deposit_withdraw_date
     */
    public void setDepositWithdrawDate(String depositWithdrawDate) {
        this.depositWithdrawDate = depositWithdrawDate;
    }

    /**
     * 
     * @return
     *     The depositWithdrawStatus
     */
    public String getDepositWithdrawStatus() {
        return depositWithdrawStatus;
    }

    /**
     * 
     * @param depositWithdrawStatus
     *     The deposit_withdraw_status
     */
    public void setDepositWithdrawStatus(String depositWithdrawStatus) {
        this.depositWithdrawStatus = depositWithdrawStatus;
    }

    /**
     * 
     * @return
     *     The depositNotes
     */
    public String getDepositNotes() {
        return Html.fromHtml(depositNotes).toString();
    }

    /**
     * 
     * @param depositNotes
     *     The deposit_notes
     */
    public void setDepositNotes(String depositNotes) {
        this.depositNotes = depositNotes;
    }

}
