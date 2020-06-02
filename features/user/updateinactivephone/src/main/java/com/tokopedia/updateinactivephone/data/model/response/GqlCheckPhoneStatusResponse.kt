package com.tokopedia.updateinactivephone.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlCheckPhoneStatusResponse (
    @SerializedName("validateInactivePhone")
    @Expose
    var validateInactivePhone: ValidateInactivePhone = ValidateInactivePhone()
)

data class ValidateInactivePhone (
    @SerializedName("user_id")
    @Expose
    var userId: String = "",

    @SerializedName("__typename")
    @Expose
    var typeName: String = "",

    @SerializedName("is_success")
    @Expose
    var isSuccess: Boolean = false,

    @SerializedName("error")
    @Expose
    var error: String = ""
)
