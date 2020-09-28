package com.tokopedia.logout.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 5/30/18.
 */
data class LogoutDataModel(
        @Expose
        @SerializedName("logout_user")
        val response: Response = Response()
) {
    data class Response(
            @Expose
            @SerializedName("success")
            val success: Boolean = false,
            @Expose
            @SerializedName("errors")
            val errors: MutableList<Errors> = mutableListOf()
    )

    data class Errors(
            @Expose
            @SerializedName("name")
            val success: String = "",
            @Expose
            @SerializedName("message")
            val message: String = ""
    )
}