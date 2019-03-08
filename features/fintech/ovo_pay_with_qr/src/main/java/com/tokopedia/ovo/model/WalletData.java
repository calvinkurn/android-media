package com.tokopedia.ovo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletData {
    @SerializedName("wallet")
    @Expose
    private Wallet wallet;

    public Wallet getWallet(){
        return wallet;
    }
}
