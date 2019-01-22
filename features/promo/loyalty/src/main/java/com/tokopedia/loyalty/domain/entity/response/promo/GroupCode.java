package com.tokopedia.loyalty.domain.entity.response.promo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 09/01/18.
 */

public class GroupCode {
    @SerializedName("single_code")
    @Expose
    private String singleCode;

    public String getSingleCode() {
        return singleCode;
    }
}
