package com.tokopedia.updateinactivephone.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlUpdatePhoneStatusResponse (
    @SerializedName("changeInactivePhone")
    @Expose
    var changeInactivePhoneQuery: ChangeInactivePhoneQuery? = null

)

data class ChangeInactivePhoneQuery (
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
