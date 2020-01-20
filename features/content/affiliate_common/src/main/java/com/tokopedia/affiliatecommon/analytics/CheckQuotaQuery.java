package com.tokopedia.affiliatecommon.analytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 12/10/18.
 */
public class CheckQuotaQuery {
    @SerializedName("affiliatePostQuota")
    @Expose
    private CheckQuotaPojo data;

    public CheckQuotaPojo getData() {
        return data;
    }

    public void setData(CheckQuotaPojo data) {
        this.data = data;
    }
}
