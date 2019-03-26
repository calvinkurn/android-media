package com.tokopedia.instantdebitbca.data.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 22/03/19.
 */
public class ResponseAccessTokenBca {
    @SerializedName("merchantAuth")
    @Expose
    private MerchantAuthEntity merchantAuth;

    public MerchantAuthEntity getMerchantAuth() {
        return merchantAuth;
    }
}
