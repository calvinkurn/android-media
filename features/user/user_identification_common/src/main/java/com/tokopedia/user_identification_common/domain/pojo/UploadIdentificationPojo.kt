package com.tokopedia.user_identification_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 13/11/18.
 */
class UploadIdentificationPojo {
    @Expose
    @SerializedName("kycUploadV3")
    val kycUpload: KycUpload = KycUpload()

    inner class KycUpload {
        @Expose
        @SerializedName("isSuccess")
        val isSuccess = 0

        @Expose
        @SerializedName("error")
        val error: String = ""

    }
}