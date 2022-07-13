package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ValidateAdminEmailResponse(
    @SerializedName("validateAdminEmail")
    @Expose
    val validateAdminEmail: ValidateAdminEmail = ValidateAdminEmail()
) {
    data class ValidateAdminEmail(
        @SerializedName("data")
        @Expose
        val `data`: Data = Data(),
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("success")
        @Expose
        val success: Boolean = false
    ) {
        data class Data(
            @SerializedName("existsUser")
            @Expose
            val existsUser: Boolean = false,
            @SerializedName("newUser")
            @Expose
            val newUser: Boolean = false,
            @SerializedName("userName")
            @Expose
            val userName: String = ""
        )
    }
}