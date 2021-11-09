package com.tokopedia.entertainment.pdp.data.redeem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorRedeem (
        @SerializedName("message_error")
        @Expose
        val messageError : List<String> = listOf("")
        )