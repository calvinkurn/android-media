package com.tokopedia.expresscheckout.domain.entity

import com.google.gson.annotations.SerializedName

data class ExpressCheckoutFormHeader(
        @SerializedName("process_time")
        val processTime: Double,

        @SerializedName("errors")
        val errors: ArrayList<String>,

        @SerializedName("reason")
        val reason: String,

        @SerializedName("error_code")
        val errorCode: String
)