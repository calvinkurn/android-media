package com.tokopedia.tkpd.tkpdreputation.createreputation.model


import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("userID")
    val userID: Int = 0,
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("userStatus")
    val userStatus: Int = 0
)