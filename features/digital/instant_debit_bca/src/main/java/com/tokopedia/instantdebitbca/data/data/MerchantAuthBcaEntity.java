package com.tokopedia.instantdebitbca.data.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class MerchantAuthBcaEntity {
    @SerializedName("data")
    @Expose
    private DataTokenBcaEntity dataToken;

    public DataTokenBcaEntity getDataToken() {
        return dataToken;
    }
}
