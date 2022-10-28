package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

data class UserProfileUpdateResponse(
    @Expose
    @SerializedName("userProfileUpdate")
    val data: UserProfileUpdate = UserProfileUpdate()
) {
    data class UserProfileUpdate(
        @Expose
        @SerializedName("isSuccess")
        val isSuccess: Int = Int.ZERO,
        @Expose
        @SerializedName("errors")
        val errors: List<String> = listOf()
    )
}