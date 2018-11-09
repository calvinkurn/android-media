package com.tokopedia.affiliate.common.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by milhamj on 10/17/18.
 */
public class AffiliateCheckData {
    @SerializedName("affiliateCheck")
    @Expose
    private AffiliateCheckPojo affiliateCheck;

    public AffiliateCheckPojo getAffiliateCheck() {
        return affiliateCheck;
    }

    public void setAffiliateCheck(AffiliateCheckPojo affiliateCheck) {
        this.affiliateCheck = affiliateCheck;
    }
}
