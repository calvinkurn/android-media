package com.tokopedia.entertainment.pdp.data.redeem.validate

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventValidateUser(
        @SuppressLint("Invalid Data Type")
        @SerializedName("product_id")
        @Expose
        val product_id : Long = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("user_id")
        @Expose
        val user_id : Long = 0,
        @SerializedName("email")
        @Expose
        val email : String = ""
)