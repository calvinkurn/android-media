package com.tokopedia.updateinactivephone.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlCheckPhoneStatusResponse (
    @SerializedName("validateInactivePhone")
    @Expose
    var validateInactivePhone: ValidateInactivePhone? = null
)

data class ValidateInactivePhone (
    @SerializedName("user_id")
    @Expose
    var userId: String? = "",

    @SerializedName("__typename")
    @Expose
    var __typename: String? = "",

    @SerializedName("is_success")
    @Expose
    val isSuccess: Boolean = false,

    @SerializedName("error")
    @Expose
    var error: String? = ""
)
