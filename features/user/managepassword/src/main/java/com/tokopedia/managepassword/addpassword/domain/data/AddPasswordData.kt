package com.tokopedia.managepassword.addpassword.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddPasswordData(
        @Expose
        @SerializedName("is_success")
        val isSuccess: Boolean = false,
        @Expose
        @SerializedName("error_code")
        val errorCode: String = "",
        @Expose
        @SerializedName("error_message")
        val errorMessage: String = ""
)
