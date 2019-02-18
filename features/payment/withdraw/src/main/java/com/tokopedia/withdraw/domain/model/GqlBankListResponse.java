package com.tokopedia.withdraw.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GqlBankListResponse {

    @SerializedName("error")
    @Expose
    private String bankAccount;

    @SerializedName("accounts")
    @Expose
    private List<BankAccount> bankAccountList;

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public List<BankAccount> getBankAccountList() {
        return bankAccountList;
    }

    public void setBankAccountList(List<BankAccount> bankAccountList) {
        this.bankAccountList = bankAccountList;
    }
}
