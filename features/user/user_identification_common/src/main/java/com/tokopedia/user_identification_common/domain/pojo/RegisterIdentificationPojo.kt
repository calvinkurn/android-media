package com.tokopedia.user_identification_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by alvinatin on 23/11/18.
 */
class RegisterIdentificationPojo {
    @Expose
    @SerializedName("kycRegister")
    var kycRegister: KycRegister = KycRegister()

    inner class KycRegister {
        @Expose
        @SerializedName("isSuccess")
        var isSuccess = 0

        @Expose
        @SerializedName("error")
        var error: String = ""

    }
}