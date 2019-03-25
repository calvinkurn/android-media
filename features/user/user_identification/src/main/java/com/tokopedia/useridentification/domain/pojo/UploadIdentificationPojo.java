package com.tokopedia.useridentification.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 13/11/18.
 */
public class UploadIdentificationPojo {

    @Expose
    @SerializedName("kycUploadImage")
    private KycUpload kycUpload;

    public KycUpload getKycUpload() {
        return kycUpload;
    }

    public class KycUpload {
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
