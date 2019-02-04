package com.tokopedia.saldodetails.response.model;

import com.google.gson.annotations.SerializedName;

public class GqlSaldoBalanceResponse {

    @SerializedName("usableBuyerSaldo")
    private Saldo usableBuyerSaldo;

    @SerializedName("usableSellerSaldo")
    private Saldo usableSellerSaldo;

    @SerializedName("holdSellerSaldo")
    private Saldo holdSellerSaldo;

    @SerializedName("holdBuyerSaldo")
    private Saldo holdBuyerSaldo;

    public Saldo getUsableBuyerSaldo() {
        return usableBuyerSaldo;
    }

    public void setUsableBuyerSaldo(Saldo usableBuyerSaldo) {
        this.usableBuyerSaldo = usableBuyerSaldo;
    }

    public Saldo getUsableSellerSaldo() {
        return usableSellerSaldo;
    }

    public void setUsableSellerSaldo(Saldo usableSellerSaldo) {
        this.usableSellerSaldo = usableSellerSaldo;
    }

    public Saldo getHoldSellerSaldo() {
        return holdSellerSaldo;
    }

    public void setHoldSellerSaldo(Saldo holdSellerSaldo) {
        this.holdSellerSaldo = holdSellerSaldo;
    }

    public Saldo getSaldo() {
        return usableBuyerSaldo;
    }

    public void setSaldo(Saldo saldo) {
        this.usableBuyerSaldo = saldo;
    }

    public Saldo getHoldBuyerSaldo() {
        return holdBuyerSaldo;
    }

    public void setHoldBuyerSaldo(Saldo holdBuyerSaldo) {
        this.holdBuyerSaldo = holdBuyerSaldo;
    }

    public class Saldo {

        @SerializedName("deposit_fmt")
        private String formattedAmount;

        @SerializedName("deposit")
        private long deposit;

        public String getFormattedAmount() {
            return formattedAmount;
        }

        public void setFormattedAmount(String formattedAmount) {
            this.formattedAmount = formattedAmount;
        }

        public long getDeposit() {
            return deposit;
        }

        public void setDeposit(long deposit) {
            this.deposit = deposit;
        }
    }
}
