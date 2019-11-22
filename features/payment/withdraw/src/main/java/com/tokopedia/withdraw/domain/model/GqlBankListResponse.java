package com.tokopedia.withdraw.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GqlBankListResponse {


    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<BankAccount> bankAccountList;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<BankAccount> getBankAccountList() {
        return bankAccountList;
    }
}
