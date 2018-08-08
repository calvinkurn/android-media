package com.tokopedia.gm.statistic.view.model;

/**
 * @author normansyahputa on 7/13/17.
 */

public class GMUnFinishedTransactionViewModel {
    private long totalTransactionCount;

    private String onHoldAmountText;

    private long onHoldCount;
    private long resoCount;
    private long onHoldAmount;

    private String formatAmount;

    public GMUnFinishedTransactionViewModel(long onHoldCount, long resoCount, long onHoldAmount, String formatAmount) {
        this.onHoldCount = onHoldCount;
        this.resoCount = resoCount;
        this.onHoldAmount = onHoldAmount;

        totalTransactionCount = resoCount + onHoldCount;

        this.formatAmount = formatAmount;

        this.onHoldAmountText = String.format(formatAmount, onHoldAmount);
    }

    public GMUnFinishedTransactionViewModel(long onHoldCount, long resoCount, long onHoldAmount) {
        this.onHoldCount = onHoldCount;
        this.resoCount = resoCount;
        this.onHoldAmount = onHoldAmount;

        totalTransactionCount = resoCount + onHoldCount;
    }

    public void setFormatAmount(String formatAmount) {
        this.formatAmount = formatAmount;
    }

    public void formatText() {
        this.onHoldAmountText = String.format(formatAmount, onHoldAmount);
    }

    public long getTotalTransactionCount() {
        return totalTransactionCount;
    }

    public String getOnHoldAmountText() {
        return onHoldAmountText;
    }

    public long getOnHoldCount() {
        return onHoldCount;
    }

    public long getResoCount() {
        return resoCount;
    }

    public long getOnHoldAmount() {
        return onHoldAmount;
    }
}
