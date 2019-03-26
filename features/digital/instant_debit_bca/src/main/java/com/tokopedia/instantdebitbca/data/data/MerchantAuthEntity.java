package com.tokopedia.instantdebitbca.data.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class MerchantAuthEntity {
    @SerializedName("data")
    @Expose
    private DataTokenEntity dataToken;

    public DataTokenEntity getDataToken() {
        return dataToken;
    }
}
