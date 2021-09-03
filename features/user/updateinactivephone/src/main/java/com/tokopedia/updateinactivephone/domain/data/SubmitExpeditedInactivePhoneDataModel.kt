package com.tokopedia.updateinactivephone.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitExpeditedInactivePhoneDataModel(
    @Expose
    @SerializedName("message_error")
    var errorMessage: String = "",
    @Expose
    @SerializedName("is_success")
    var isSuccess: Boolean = false
)