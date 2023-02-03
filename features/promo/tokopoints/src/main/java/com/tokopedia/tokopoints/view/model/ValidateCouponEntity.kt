package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateCouponEntity(
    @Expose
    @SerializedName("is_valid")
    var isValid: Int = 0,
    @Expose
    @SerializedName("message_success")
    var messageSuccess: String = "",
    @Expose
    @SerializedName("message_title")
    var messageTitle: String = "",
)
