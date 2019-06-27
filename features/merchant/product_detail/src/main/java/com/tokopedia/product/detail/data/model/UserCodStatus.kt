package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserCodStatus(
        @SerializedName("success")
        @Expose
        val success: Int = 0,

        @SerializedName("error")
        @Expose
        val error: String = "",

        @SerializedName("isCod")
        @Expose
        val isCod: Boolean = false
) {
    data class Data(
            @SerializedName("data")
            @Expose
            val userCodStatus: UserCodStatus = UserCodStatus()
    )

    data class Response(
            @SerializedName("cod_validation")
            @Expose
            val result: Data = Data()
    )
}