
package com.tokopedia.core.deposit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Summary {

    @SerializedName("summary_hold_deposit_idr")
    @Expose
    private String summaryHoldDepositIdr;
    @SerializedName("summary_total_deposit_idr")
    @Expose
    private String summaryTotalDepositIdr;
    @SerializedName("summary_total_deposit")
    @Expose
    private int summaryTotalDeposit;
    @SerializedName("summary_deposit_hold_tx_1_day")
    @Expose
    private String summaryDepositHoldTx1Day;
    @SerializedName("summary_today_tries")
    @Expose
    private int summaryTodayTries;
    @SerializedName("summary_useable_deposit_idr")
    @Expose
    private String summaryUseableDepositIdr;
    @SerializedName("summary_useable_deposit")
    @Expose
    private int summaryUseableDeposit;
    @SerializedName("summary_deposit_hold_tx_1_day_idr")
    @Expose
    private String summaryDepositHoldTx1DayIdr;
    @SerializedName("summary_hold_deposit")
    @Expose
    private int summaryHoldDeposit;
    @SerializedName("summary_daily_tries")
    @Expose
    private int summaryDailyTries;
    @SerializedName("summary_deposit_hold_by_cs")
    @Expose
    private String summaryDepositHoldByCs;
    @SerializedName("summary_deposit_hold_by_cs_idr")
    @Expose
    private String summaryDepositHoldByCsIdr;

    /**
     * 
     * @return
     *     The summaryHoldDepositIdr
     */
    public String getSummaryHoldDepositIdr() {
        return summaryHoldDepositIdr;
    }

    /**
     * 
     * @param summaryHoldDepositIdr
     *     The summary_hold_deposit_idr
     */
    public void setSummaryHoldDepositIdr(String summaryHoldDepositIdr) {
        this.summaryHoldDepositIdr = summaryHoldDepositIdr;
    }

    /**
     * 
     * @return
     *     The summaryTotalDepositIdr
     */
    public String getSummaryTotalDepositIdr() {
        return summaryTotalDepositIdr;
    }

    /**
     * 
     * @param summaryTotalDepositIdr
     *     The summary_total_deposit_idr
     */
    public void setSummaryTotalDepositIdr(String summaryTotalDepositIdr) {
        this.summaryTotalDepositIdr = summaryTotalDepositIdr;
    }

    /**
     * 
     * @return
     *     The summaryTotalDeposit
     */
    public int getSummaryTotalDeposit() {
        return summaryTotalDeposit;
    }

    /**
     * 
     * @param summaryTotalDeposit
     *     The summary_total_deposit
     */
    public void setSummaryTotalDeposit(int summaryTotalDeposit) {
        this.summaryTotalDeposit = summaryTotalDeposit;
    }

    /**
     * 
     * @return
     *     The summaryDepositHoldTx1Day
     */
    public String getSummaryDepositHoldTx1Day() {
        return summaryDepositHoldTx1Day;
    }

    /**
     * 
     * @param summaryDepositHoldTx1Day
     *     The summary_deposit_hold_tx_1_day
     */
    public void setSummaryDepositHoldTx1Day(String summaryDepositHoldTx1Day) {
        this.summaryDepositHoldTx1Day = summaryDepositHoldTx1Day;
    }

    /**
     * 
     * @return
     *     The summaryTodayTries
     */
    public int getSummaryTodayTries() {
        return summaryTodayTries;
    }

    /**
     * 
     * @param summaryTodayTries
     *     The summary_today_tries
     */
    public void setSummaryTodayTries(int summaryTodayTries) {
        this.summaryTodayTries = summaryTodayTries;
    }

    /**
     * 
     * @return
     *     The summaryUseableDepositIdr
     */
    public String getSummaryUseableDepositIdr() {
        return summaryUseableDepositIdr;
    }

    /**
     * 
     * @param summaryUseableDepositIdr
     *     The summary_useable_deposit_idr
     */
    public void setSummaryUseableDepositIdr(String summaryUseableDepositIdr) {
        this.summaryUseableDepositIdr = summaryUseableDepositIdr;
    }

    /**
     * 
     * @return
     *     The summaryUseableDeposit
     */
    public int getSummaryUseableDeposit() {
        return summaryUseableDeposit;
    }

    /**
     * 
     * @param summaryUseableDeposit
     *     The summary_useable_deposit
     */
    public void setSummaryUseableDeposit(int summaryUseableDeposit) {
        this.summaryUseableDeposit = summaryUseableDeposit;
    }

    /**
     * 
     * @return
     *     The summaryDepositHoldTx1DayIdr
     */
    public String getSummaryDepositHoldTx1DayIdr() {
        return summaryDepositHoldTx1DayIdr;
    }

    /**
     * 
     * @param summaryDepositHoldTx1DayIdr
     *     The summary_deposit_hold_tx_1_day_idr
     */
    public void setSummaryDepositHoldTx1DayIdr(String summaryDepositHoldTx1DayIdr) {
        this.summaryDepositHoldTx1DayIdr = summaryDepositHoldTx1DayIdr;
    }

    /**
     * 
     * @return
     *     The summaryHoldDeposit
     */
    public int getSummaryHoldDeposit() {
        return summaryHoldDeposit;
    }

    /**
     * 
     * @param summaryHoldDeposit
     *     The summary_hold_deposit
     */
    public void setSummaryHoldDeposit(int summaryHoldDeposit) {
        this.summaryHoldDeposit = summaryHoldDeposit;
    }

    /**
     * 
     * @return
     *     The summaryDailyTries
     */
    public int getSummaryDailyTries() {
        return summaryDailyTries;
    }

    /**
     * 
     * @param summaryDailyTries
     *     The summary_daily_tries
     */
    public void setSummaryDailyTries(int summaryDailyTries) {
        this.summaryDailyTries = summaryDailyTries;
    }

    /**
     * 
     * @return
     *     The summaryDepositHoldByCs
     */
    public String getSummaryDepositHoldByCs() {
        return summaryDepositHoldByCs;
    }

    /**
     * 
     * @param summaryDepositHoldByCs
     *     The summary_deposit_hold_by_cs
     */
    public void setSummaryDepositHoldByCs(String summaryDepositHoldByCs) {
        this.summaryDepositHoldByCs = summaryDepositHoldByCs;
    }

    /**
     * 
     * @return
     *     The summaryDepositHoldByCsIdr
     */
    public String getSummaryDepositHoldByCsIdr() {
        return summaryDepositHoldByCsIdr;
    }

    /**
     * 
     * @param summaryDepositHoldByCsIdr
     *     The summary_deposit_hold_by_cs_idr
     */
    public void setSummaryDepositHoldByCsIdr(String summaryDepositHoldByCsIdr) {
        this.summaryDepositHoldByCsIdr = summaryDepositHoldByCsIdr;
    }

}
