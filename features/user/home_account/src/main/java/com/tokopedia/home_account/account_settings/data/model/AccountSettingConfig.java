package com.tokopedia.home_account.account_settings.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 13/11/18.
 */
public class AccountSettingConfig {

    @SerializedName("accountSettingConfig")
    @Expose
    private AccountSettingConfigResponse  accountSettingConfig = new AccountSettingConfigResponse();

    public AccountSettingConfigResponse getAccountSettingConfig() {
        return accountSettingConfig;
    }
}


