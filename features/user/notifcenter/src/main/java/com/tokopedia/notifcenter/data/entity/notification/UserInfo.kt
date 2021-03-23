package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("email")
    val email: String = "",
    @SerializedName("fullname")
    val fullname: String = "",
    @SerializedName("shop_id")
    val shopId: Long = 0,
    @SerializedName("user_id")
    val userId: Long = 0
)