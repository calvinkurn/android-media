package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaldoModel {

    @SerializedName(value = "saldo", alternate = {"balance"})
    @Expose
    private DepositModel saldo = new DepositModel();

    public DepositModel getSaldo() {
        return saldo;
    }

    public void setSaldo(DepositModel saldo) {
        this.saldo = saldo;
    }

}
