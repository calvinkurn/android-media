package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 13/11/18.
 */
public class AccountSettingConfig {

    @SerializedName("accountSettingConfig")
    @Expose
    private AccountSettingConfigResponse  accountSettingConfig;

    public AccountSettingConfigResponse getAccountSettingConfig() {
        return accountSettingConfig;
    }
}


