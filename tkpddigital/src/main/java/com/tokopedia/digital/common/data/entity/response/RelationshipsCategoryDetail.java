package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 4/28/17.
 */

public class RelationshipsCategoryDetail {

    @SerializedName("banner")
    @Expose
    private Banner banner;
    @SerializedName("operator")
    @Expose
    private Operator operator;

    public Banner getBanner() {
        return banner;
    }

    public Operator getOperator() {
        return operator;
    }
}
