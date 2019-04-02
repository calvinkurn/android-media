package com.tokopedia.useridentification.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvinatin on 23/11/18.
 */

public class RegisterIdentificationPojo {

    @Expose
    @SerializedName("kycRegister")
    private KycRegister kycRegister;

    public KycRegister getKycRegister() {
        return kycRegister;
    }

    public class KycRegister {
        @Expose
        @SerializedName("isSuccess")
        private int isSuccess;
        @Expose
        @SerializedName("error")
        private String error;

        public int getIsSuccess() {
            return isSuccess;
        }

        public String getError() {
            return error;
        }
    }
}
