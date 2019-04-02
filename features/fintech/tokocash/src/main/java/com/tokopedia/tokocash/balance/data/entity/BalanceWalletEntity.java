package com.tokopedia.tokocash.balance.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 14/09/18.
 */
public class BalanceWalletEntity {

    @SerializedName("wallet")
    @Expose
    private BalanceTokoCashEntity wallet;

    public BalanceTokoCashEntity getWallet() {
        return wallet;
    }
}
