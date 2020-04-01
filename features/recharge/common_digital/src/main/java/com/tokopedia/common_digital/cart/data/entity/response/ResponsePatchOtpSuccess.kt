package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 4/4/17.
 */

class ResponsePatchOtpSuccess {

    @SerializedName("success")
    @Expose
    var isSuccess: Boolean = false
}
