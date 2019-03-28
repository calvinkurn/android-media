package com.tokopedia.instantdebitbca.data.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 26/03/19.
 */
public class DebitRegisterBcaEntity {
    @SerializedName("callbackURL")
    @Expose
    private String callbackUrl;
    @SerializedName("debitData")
    @Expose
    private String debitData;

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getDebitData() {
        return debitData;
    }
}
