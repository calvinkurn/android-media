package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("email")
    val email: String = "",
    @SerializedName("fullname")
    val fullname: String = ""
)