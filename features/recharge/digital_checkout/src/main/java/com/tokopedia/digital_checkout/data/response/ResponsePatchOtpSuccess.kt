package com.tokopedia.digital_checkout.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 20/01/21
 */
class ResponsePatchOtpSuccess (
        @SerializedName("success")
        @Expose
        var success: Boolean = false
)