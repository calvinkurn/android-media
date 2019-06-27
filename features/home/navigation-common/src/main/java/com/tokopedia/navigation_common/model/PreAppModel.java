package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meta on 17/09/18.
 */
public class PreAppModel {

    @SerializedName("valid")
    @Expose
    private String valid = "";

    @SerializedName("partnerName")
    @Expose
    private String partnerName = "";

    @SerializedName("partnerMaxLoan")
    @Expose
    private String partnerMaxLoan = "";

    @SerializedName("partnerMinRate")
    @Expose
    private String partnerMinRate = "";

    public String getValid() {
        return valid;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getPartnerMaxLoan() {
        return partnerMaxLoan;
    }

    public String getPartnerMinRate() {
        return partnerMinRate;
    }
}
