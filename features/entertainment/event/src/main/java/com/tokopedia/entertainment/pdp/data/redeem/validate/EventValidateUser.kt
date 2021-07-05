package com.tokopedia.entertainment.pdp.data.redeem.validate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventValidateUser(
        @SerializedName("product_id")
        @Expose
        val product_id : Int = 0,
        @SerializedName("user_id")
        @Expose
        val user_id : Int = 0,
        @SerializedName("email")
        @Expose
        val email : String = ""
)