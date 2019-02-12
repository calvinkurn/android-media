package com.tokopedia.expresscheckout.data.entity.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class Header(
        @SerializedName("process_time")
        val processTime: Double,

        @SerializedName("errors")
        val errors: ArrayList<String>,

        @SerializedName("reason")
        val reason: String,

        @SerializedName("error_code")
        val errorCode: String,

        @SerializedName("messages")
        val messages: ArrayList<String>
)