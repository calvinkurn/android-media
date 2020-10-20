package com.tokopedia.pms.proof.model

import com.google.gson.annotations.SerializedName

data class PaymentProofResponse (
        @SerializedName("code")
        var code : Int,
        @SerializedName("message_error")
        var messageError : String? = null,
        @SerializedName("status")
        var status : String? = null
)