package com.tokopedia.withdraw.domain.model.premiumAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GqlCheckPremiumAccountResponse {

    @SerializedName("CheckEligible")
    @Expose
    private CheckEligible checkEligible;

    public CheckEligible getCheckEligible() {
        return checkEligible;
    }

    public void setCheckEligible(CheckEligible checkEligible) {
        this.checkEligible = checkEligible;
    }

}
