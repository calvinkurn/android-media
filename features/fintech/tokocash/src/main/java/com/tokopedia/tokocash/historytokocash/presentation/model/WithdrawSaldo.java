package com.tokopedia.tokocash.historytokocash.presentation.model;

/**
 * Created by nabillasabbaha on 10/20/17.
 */

public class WithdrawSaldo {

    private long amount;
    private String destEmail;
    private int withdrawalId;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDestEmail() {
        return destEmail;
    }

    public void setDestEmail(String destEmail) {
        this.destEmail = destEmail;
    }

    public int getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(int withdrawalId) {
        this.withdrawalId = withdrawalId;
    }
}