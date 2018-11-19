package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountSettingConfigResponse {

    @SerializedName("data")
    @Expose
    private AccountSettingConfigResponseData  data;

    public AccountSettingConfigResponseData getData() {
        return data;
    }
}
