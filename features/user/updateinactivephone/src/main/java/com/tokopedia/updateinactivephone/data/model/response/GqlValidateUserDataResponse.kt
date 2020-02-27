package com.tokopedia.updateinactivephone.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlValidateUserDataResponse (
    @SerializedName("validateInactiveNewPhone")
    @Expose
    var validateUserDataResponse: ValidateUserDataResponse? = null
)

data class ValidateUserDataResponse (
    @SerializedName("__typename")
    @Expose
    var __typename: String? = "",

    @SerializedName("is_success")
    @Expose
    val isSuccess: Boolean = false,

    @SerializedName("user_id")
    @Expose
    val userId: Int = 0,

    @SerializedName("error")
    @Expose
    var error: String? = ""
)
