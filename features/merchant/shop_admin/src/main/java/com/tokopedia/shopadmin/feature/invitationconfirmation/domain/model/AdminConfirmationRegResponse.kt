package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminConfirmationRegResponse(
    @SerializedName("success")
    @Expose
    val success: Boolean = false,
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("acceptBecomeAdmin")
    @Expose
    val acceptBecomeAdmin: Boolean = false
)