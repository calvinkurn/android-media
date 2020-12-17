package com.tokopedia.entertainment.pdp.data.redeem.validate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class EventValidateResponse(
    @SerializedName("data")
    @Expose
    val data : Data = Data(),
    @SerializedName("server_process_time")
    @Expose
    val serverProcessTime: String = "",
    @SerializedName("status")
    @Expose
    val status: String = ""
)

data class Data(
        @SerializedName("email")
        @Expose
        val email: String = "",
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("product_id")
        @Expose
        val productId: Int = 0,
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("success")
        @Expose
        val success: Boolean = false,
        @SerializedName("user_id")
        @Expose
        val userId: Int = 0
)