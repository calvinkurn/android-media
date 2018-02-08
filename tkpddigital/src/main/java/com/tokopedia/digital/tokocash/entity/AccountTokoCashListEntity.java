package com.tokopedia.digital.tokocash.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 10/27/17.
 */

public class AccountTokoCashListEntity {

    @SerializedName("account_list")
    @Expose
    private List<AccountTokoCashEntity> account_list;

    public List<AccountTokoCashEntity> getAccount_list() {
        return account_list;
    }

    public void setAccount_list(List<AccountTokoCashEntity> account_list) {
        this.account_list = account_list;
    }
}
