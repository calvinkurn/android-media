package com.tokopedia.saldodetails.response.model;

import com.google.gson.annotations.SerializedName;

public class GqlHoldSaldoBalanceResponse {

    @SerializedName("saldo")
    private GqlSaldoBalanceResponse.Saldo saldo;

    public GqlSaldoBalanceResponse.Saldo getSaldo() {
        return saldo;
    }

    public void setSaldo(GqlSaldoBalanceResponse.Saldo saldo) {
        this.saldo = saldo;
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
