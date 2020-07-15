package com.tokopedia.updateinactivephone.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlUpdatePhoneStatusResponse (
    @SerializedName("changeInactivePhone")
    @Expose
    var changeInactivePhoneQuery: ChangeInactivePhoneQuery = ChangeInactivePhoneQuery()
)

data class ChangeInactivePhoneQuery (
    @SerializedName("__typename")
    @Expose
    var __typename: String = "",

    @SerializedName("is_success")
    @Expose
    var isSuccess: Int = 0,

    @SerializedName("error")
    @Expose
    var error: String = ""
)
