package com.tokopedia.affiliate.feature.dashboard.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 01/10/18.
 */
public class DashboardAffiliateCheck {

    @SerializedName("isAffiliate")
    @Expose
    private boolean isAffiliate;

    @SerializedName("status")
    @Expose
    private String status;

    public boolean isAffiliate() {
        return isAffiliate;
    }

    public void setAffiliate(boolean affiliate) {
        isAffiliate = affiliate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
