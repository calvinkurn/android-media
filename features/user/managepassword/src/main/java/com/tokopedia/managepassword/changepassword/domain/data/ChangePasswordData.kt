package com.tokopedia.managepassword.changepassword.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChangePasswordData(
        @Expose
        @SerializedName("user_id")
        val userId: String = "0",
        @Expose
        @SerializedName("updated")
        val isUpdated: Boolean = false,
        @Expose
        @SerializedName("type")
        val type: String = "",
        @Expose
        @SerializedName("message")
        val message: String = ""
)
