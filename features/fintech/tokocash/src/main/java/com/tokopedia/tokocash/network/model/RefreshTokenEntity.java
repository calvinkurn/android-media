package com.tokopedia.tokocash.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 11/17/17.
 */

public class RefreshTokenEntity {

    @SerializedName("data")
    @Expose
    private WalletTokenEntity data;

    public WalletTokenEntity getData() {
        return data;
    }

    public void setData(WalletTokenEntity data) {
        this.data = data;
    }
}
