package com.tokopedia.withdraw.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GqlGetBankDataResponse {

    @SerializedName("bankAccount")
    @Expose
    private GqlBankListResponse bankAccount;

    public GqlBankListResponse getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(GqlBankListResponse bankAccount) {
        this.bankAccount = bankAccount;
    }
}
