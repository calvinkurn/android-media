package com.tokopedia.navigation_common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class TokopointsModel {
    @SerializedName("status")
    @Expose
    private TokopointStatusModel status;

    public TokopointStatusModel getStatus() {
        return status;
    }

    public void setStatus(TokopointStatusModel status) {
        this.status = status;
    }
}
