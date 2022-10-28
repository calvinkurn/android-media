package com.tokopedia.additional_check.data

import com.google.gson.annotations.SerializedName

data class GetSimpleProfileResponse(
    @SerializedName("profile")
    val data: SimpleProfileData = SimpleProfileData()
)

data class SimpleProfileData(
    @SerializedName("full_name")
    val fullName: String = "",
    @SerializedName("profilePicture")
    val profilePicture: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("email")
    val email: String = ""
)

