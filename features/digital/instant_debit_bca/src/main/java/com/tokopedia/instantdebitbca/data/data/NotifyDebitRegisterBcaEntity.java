package com.tokopedia.instantdebitbca.data.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 26/03/19.
 */
public class NotifyDebitRegisterBcaEntity {
    @SerializedName("data")
    @Expose
    private DebitRegisterBcaEntity debitRegister;

    public DebitRegisterBcaEntity getDebitRegister() {
        return debitRegister;
    }
}
