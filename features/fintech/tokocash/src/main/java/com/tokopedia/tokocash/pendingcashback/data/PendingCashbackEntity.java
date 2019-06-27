package com.tokopedia.tokocash.pendingcashback.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 2/7/18.
 */

public class PendingCashbackEntity {

    @SerializedName("balance")
    @Expose
    private String balance;

    @SerializedName("balance_text")
    @Expose
    private String balanceText;

    @SerializedName("cash_balance")
    @Expose
    private String cashBalance;

    @SerializedName("cash_balance_text")
    @Expose
    private String cashBalanceText;

    @SerializedName("point_balance")
    @Expose
    private String pointBalance;

    @SerializedName("point_balance_text")
    @Expose
    private String pointBalanceText;

    @SerializedName("wallet_type")
    @Expose
    private String walletType;

    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    public String getBalance() {
        return balance;
    }

    public String getBalanceText() {
        return balanceText;
    }

    public String getCashBalance() {
        return cashBalance;
    }

    public String getCashBalanceText() {
        return cashBalanceText;
    }

    public String getPointBalance() {
        return pointBalance;
    }

    public String getPointBalanceText() {
        return pointBalanceText;
    }

    public String getWalletType() {
        return walletType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
