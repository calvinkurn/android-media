package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountSettingConfigResponseData {

    @SerializedName("android")
    @Expose
    private AccountSettingConfigAndroid accountSettingConfigAndroid;

    public AccountSettingConfigAndroid getAccountSettingConfigAndroid() {
        return accountSettingConfigAndroid;
    }
}

