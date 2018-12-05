package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountSettingConfigResponse {

    @SerializedName("dataDiri")
    @Expose
    private boolean isPeopleDataEnabled = true;

    @SerializedName("daftarAlamat")
    @Expose
    private boolean isAddressEnabled = true;

    @SerializedName("tokopediaCorner")
    @Expose
    private boolean isTokopediaCornerEnabled = false;

    @SerializedName("dokumenDataDiri")
    @Expose
    private boolean isIdentityEnabled = false;

    @SerializedName("ubahKataSandi")
    @Expose
    private boolean isPasswordEnabled = true;

    public boolean isAddressEnabled() {
        return isAddressEnabled;
    }

    public boolean isPeopleDataEnabled() {
        return isPeopleDataEnabled;
    }

    public boolean isTokopediaCornerEnabled() {
        return isTokopediaCornerEnabled;
    }

    public boolean isIdentityEnabled() {
        return isIdentityEnabled;
    }

    public boolean isPasswordEnabled() {
        return isPasswordEnabled;
    }
}
