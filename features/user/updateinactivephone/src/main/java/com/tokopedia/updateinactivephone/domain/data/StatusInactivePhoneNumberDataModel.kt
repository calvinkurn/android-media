package com.tokopedia.updateinactivephone.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StatusInactivePhoneNumberDataModel(
    @Expose
    @SerializedName("GetStatusInactivePhoneNumber")
    var statusInactivePhoneNumber: GetStatusInactivePhoneNumber = GetStatusInactivePhoneNumber()
)

data class GetStatusInactivePhoneNumber(
    @Expose
    @SerializedName("error_message")
    var errorMessage: String = "",
    @Expose
    @SerializedName("is_success")
    var isSuccess: Boolean = false,
    @Expose
    @SerializedName("is_allowed")
    var isAllowed: Boolean = false,
    @Expose
    @SerializedName("userid_enc")
    var userIdEnc: String = ""
)