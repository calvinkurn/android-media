package com.tokopedia.logout.domain.model

import com.google.gson.annotations.SerializedName

data class LogoutDataModel(
        @SerializedName("logout_user")
        val response: Response = Response()
) {
    data class Response(
            @SerializedName("success")
            val success: Boolean = false,
            @SerializedName("errors")
            val errors: MutableList<Errors> = mutableListOf()
    )

    data class Errors(
            val success: String = "",
            @SerializedName("message")
            val message: String = ""
    )
}
