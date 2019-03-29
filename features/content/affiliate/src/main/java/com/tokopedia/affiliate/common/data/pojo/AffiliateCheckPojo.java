package com.tokopedia.affiliate.common.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 12/10/18.
 */
public class AffiliateCheckPojo {

    /**
     * isAffiliate : false
     * status : Not Affiliate
     */

    @SerializedName("isAffiliate")
    private boolean isAffiliate;

    @SerializedName("status")
    private String status;

    public boolean isIsAffiliate() {
        return isAffiliate;
    }

    public void setIsAffiliate(boolean isAffiliate) {
        this.isAffiliate = isAffiliate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
