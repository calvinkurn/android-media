
package com.tokopedia.tkpd.msisdn.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerificationForm {

    @SerializedName("msisdn")
    @Expose
    private MSISDN msisdn;

    /**
     * 
     * @return
     *     The msisdn
     */
    public MSISDN getMsisdn() {
        return msisdn;
    }

    /**
     * 
     * @param msisdn
     *     The msisdn
     */
    public void setMsisdn(MSISDN msisdn) {
        this.msisdn = msisdn;
    }

}
