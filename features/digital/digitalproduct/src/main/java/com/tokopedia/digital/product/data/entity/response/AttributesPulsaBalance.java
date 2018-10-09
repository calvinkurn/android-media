package com.tokopedia.digital.product.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ashwanityagi on 18/07/17.
 */

public class AttributesPulsaBalance {
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("balance_plain")
    @Expose
    private String balancePlain;
    @SerializedName("expire_date")
    @Expose
    private String expireDate;
    @SerializedName("success")
    @Expose
    private boolean success;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalancePlain() {
        return balancePlain;
    }

    public void setBalancePlain(String balancePlain) {
        this.balancePlain = balancePlain;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
